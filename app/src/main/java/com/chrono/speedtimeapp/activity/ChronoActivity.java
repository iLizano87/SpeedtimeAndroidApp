package com.chrono.speedtimeapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chrono.speedtimeapp.R;
import com.chrono.speedtimeapp.api.ApiService;
import com.chrono.speedtimeapp.model.Time;
import com.chrono.speedtimeapp.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChronoActivity extends AppCompatActivity {

    private String name, parcial1, parcial2, kmh;
    private long lapTime, id, idUser, idTrack, startTime, tiempoIniciado, currentTime, lastLapTime, parcialTime1, parcialTime2;
    private float distanciaTotal, distanciaInicio, distanciaFin, distanciaRecorridaActual;
    private float distanciaRecorridaTotal = 0f;
    private TextView timerTextView, resultadoTextView, vueltasTextView, velocidadTextView, listaTextView, parcial1TextView, parcial2TextView;
    private Handler handler;
    private Runnable runnable;
    private int minutos, segundos, milisegundos;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double iniLat, iniLong, p1Lat, p1Long, p2Lat, p2Long, lastLapSpeed;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Button btnStop, btnReset, btnSalir;
    private boolean isRunning = false;
    private boolean isRecordingLap = false;
    private boolean isPassing;
    private long elapsedTime = 0;
    private boolean hasPassed = false;
    private Location targetLocation, p1Location, p2Location;
    private ApiService apiService;
    private List<String> vueltasList = new ArrayList<>();

    private int lastUpdate = 0;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.cfwhite));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        getWindow().setBackgroundDrawable(bitmapDrawable);

        setContentView(R.layout.chrono_view);

        lastLapTime = System.currentTimeMillis();
        timerTextView = findViewById(R.id.timerTextView);
        vueltasTextView = findViewById(R.id.vueltasTextView);
        velocidadTextView = findViewById(R.id.velocidadTextView);
        listaTextView = findViewById(R.id.listaTextView);
        parcial1TextView = findViewById(R.id.parcial1TextView);
        parcial2TextView = findViewById(R.id.parcial2TextView);
        TextView nameTextView = findViewById(R.id.nameTextView);

        //DEFINICION DE BOTON Y ACCION
        btnSalir = findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la actividad WebActivity
                Intent intent = new Intent(ChronoActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            // Obtener los valores de idUser y idTrack
            String userId = intent.getStringExtra("userId");
            String trackId = intent.getStringExtra("trackId");
            Long idUser = Long.valueOf(userId);
            Long idTrack = Long.valueOf(trackId);
            Log.d("URL", "idUser: " + idUser);
            Log.d("URL", "idTrack: " + idTrack);

            //LLAMADA AL TRACK INDICADO
            Call<Track> call = getTrackCall(idTrack);
            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
                        Track track = response.body();
                        // VALORES OBTENIDOS DE LA BD
                        iniLat = track.getIniLat();
                        iniLong = track.getIniLong();
                        p1Lat = track.getP1Lat();
                        p1Long = track.getP1Long();
                        p2Lat = track.getP2Lat();
                        p2Long = track.getP2Long();
                        name = track.getName();

                        nameTextView.setText(name);

                        targetLocation = new Location("puntoA");
                        targetLocation.setLatitude(iniLat);
                        targetLocation.setLongitude(iniLong);

                        p1Location = new Location("puntoB");
                        p1Location.setLatitude(p1Lat);
                        p1Location.setLongitude(p1Long);

                        p2Location = new Location("puntoC");
                        p2Location.setLatitude(p2Lat);
                        p2Location.setLongitude(p2Long);

                    } else {
                        nameTextView.setText("ERROR");
                    }
                }

                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                }
            });

            vueltasTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No se necesita implementación
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // No se necesita implementación
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //TRAS MODIFICAR TIEMPO DE VUELTA SE SUBE EL TIEMPO
                    String vueltasText = vueltasTextView.getText().toString();
                    id = 0;
                    Time time = new Time(id, idUser, idTrack, lapTime, parcialTime1, parcialTime2, kmh, date());
                    Log.d("Time", id + " - " + idUser + " - " + idTrack + " - " + lapTime);
                    distanciaRecorridaActual = 0;
                    distanciaRecorridaTotal = 0;

                    if (!vueltasText.isEmpty()) {
                        try {
                            vueltasList.add(String.valueOf(formatTime(lapTime)));
                            StringBuilder sb = new StringBuilder();
                            for (int i = vueltasList.size() - 1; i >= 0; i--) {
                                String tiempo = vueltasList.get(i);
                                sb.append(tiempo);
                                sb.append("\n"); // Agregar salto de línea para separar los tiempos
                            }
                            // Asignar la cadena de texto al TextView
                            listaTextView.setText(sb.toString());
                            Log.d("Arraylist times", "agregada vuelta: " + lapTime);
                            Log.d("LISTADO COMPLETO", "Lista " + vueltasList.toString());
                        } catch (NumberFormatException e) {
                            // Manejar la excepción si la cadena no es un número válido
                            Log.e("NumberFormatException", "La cadena no es un número válido");
                        }
                    }
                    if (lapTime != 0) {
                        Call<Void> call = apiService.postData(time);
                        call.enqueue(new Callback<Void>() {
                                         @Override
                                         public void onResponse(Call<Void> call, Response<Void> response) {
                                             if (response.isSuccessful()) {
                                                 Log.d("API", "Solicitud POST exitosa");
                                                 parcial1TextView.setText("P1: ");
                                                 parcial2TextView.setText("P2: ");
                                             } else {
                                                 Log.d("API", "Error en la solicitud POST: " + response.code());
                                             }
                                         }

                                         @Override
                                         public void onFailure(Call<Void> call, Throwable t) {
                                             Log.e("API", "Error al realizar la solicitud POST", t);
                                         }
                                     }
                        );
                    }
                }
            });

            float[] results = new float[1];
            Location.distanceBetween(iniLat, iniLong, p1Lat, p1Long, results);
            distanciaTotal = results[0];

            final float[] distanciaRecorrida = {0f};

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    tiempoIniciado = System.currentTimeMillis() - startTime;
                    timerTextView.setText(formatTime(tiempoIniciado));
                    handler.postDelayed(this, 10);
                }
            };
            // En onCreate, después de calcular la distancia entre puntos:
            distanciaTotal = distanciaFin;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    long currentTime = System.currentTimeMillis();
                    double speedInKmH = location.getSpeed() * 3.6; // velocidad instantánea en km/h
                    velocidadTextView.setText(String.format("%.2f km/h", speedInKmH));
                    if (isRunning) {
                        // Calcula la distancia recorrida desde la última ubicación
                        distanciaRecorridaActual = location.distanceTo(targetLocation);
                        distanciaRecorridaTotal += distanciaRecorridaActual;
                        // Calcula la velocidad media hasta el momento
                        float velocidadMedia = distanciaRecorridaTotal / (tiempoIniciado / 100f);
                        kmh = String.format(Locale.getDefault(), "%.2f km/h", velocidadMedia);
                        // Log.d("kmh","kmh: "+kmh);
                    }

                    if (isRunning && location.distanceTo(p1Location) < 10) {
                        parcialTime1 = currentTime - startTime;
                        parcial1 = formatTime(parcialTime1);
                        parcial1TextView.setText("P1: " + parcial1);
                    }

                    if (isRunning && location.distanceTo(p2Location) < 10) {
                        parcialTime2 = currentTime - startTime;
                        parcial2 = formatTime(parcialTime2);
                        parcial2TextView.setText("P2: " + parcial2);
                    }

                    if (location.distanceTo(targetLocation) < 10) {
                        double distance = location.distanceTo(targetLocation);
                        double speed = distance / (currentTime - tiempoIniciado / 1000.0);
                        speedInKmH = speed * 3.6; // Asignar valor a la variable de instancia

                        startTime = System.currentTimeMillis();
                        handler.postDelayed(runnable, 0);
                        isRunning = true;
                        tiempoIniciado = System.currentTimeMillis();

                        if (!hasPassed) {
                            // Guardar el tiempo en que se inició la última vuelta
                            lastLapTime = currentTime;
                            vueltasTextView.setText("");
                            hasPassed = true;
                        } else {
                            // Calcular el tiempo transcurrido desde que se inició la última vuelta y actualizar vueltasTextView
                            lapTime = currentTime - lastLapTime;
                            vueltasTextView.setText(formatTime(lapTime));
                            lastLapTime = currentTime;
                            // Actualizar la velocidad de la última vuelta
                            lastLapSpeed = speedInKmH;
                        }
                    } else {
                        currentTime = System.currentTimeMillis();
                        long elapsedTime = currentTime - startTime;
                        double distance = location.distanceTo(targetLocation);
                        double speed = distance / (elapsedTime / 1000.0);
                        speedInKmH = speed * 3.6;
                        String result = String.format("Velocidad promedio: %.2f km/h",
                                speedInKmH
                        );

                        if (isPassing) {
                            long lapTime = currentTime - lastLapTime;
                            // Calcular el tiempo transcurrido desde que se inició la última vuelta y actualizar vueltasTextView
                            if (lapTime >= 20000) { // si el tiempo de vuelta es mayor o igual a 20 segundos
                                vueltasTextView.setText(formatTime(lapTime));
                                lastLapTime = currentTime;
                                // Actualizar la velocidad de la vuelta anterior
                                distance = location.distanceTo(targetLocation);
                                lastLapSpeed = distance / (lapTime / 1000.0) * 3.6;
                            }
                            if (isRunning && hasPassed) {
                                resultadoTextView.setText(result);
                            }
                        }
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            }
            ;
        }
    }

    private String date() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String fechaActual = dateFormat.format(calendar.getTime());
        String horaActual = timeFormat.format(calendar.getTime());
        String date = fechaActual + "-" + horaActual;
        return date;
    }

    private Call<Track> getTrackCall(Long idTrack) {
        // URL para realizar consulta a Track
        String baseUrl = "http://192.168.1.145:9876/";
        String trackUrl = baseUrl + "track/" + idTrack;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Al llamar a apiService obtenemos instancia de ApiService y la construimos
        apiService = retrofit.create(ApiService.class);
        Call<Track> call = apiService.getTrackById(idTrack);
        return call;
    }

    private String formatTime(long timeInMillis) {
        int minutes = (int) (timeInMillis / 1000) / 60;
        int seconds = (int) (timeInMillis / 1000) % 60;
        int milliseconds = (int) (timeInMillis % 1000);
        return String.format(Locale.getDefault(), "%02d:%02d.%03d", minutes, seconds, milliseconds);
    }
}

