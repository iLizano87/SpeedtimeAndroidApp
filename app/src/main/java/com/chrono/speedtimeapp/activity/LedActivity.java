package com.chrono.speedtimeapp.activity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.chrono.speedtimeapp.api.ApiService;

import java.util.ArrayList;
import java.util.List;

public class LedActivity {

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

}
