package net.ivanvega.audiolibros2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creamos una instancia del framento que desemos hacer
        SelectorFragment selectorFragment = new SelectorFragment();

        if (findViewById(R.id.contenedor_pequeno) != null && getSupportFragmentManager().findFragmentById(R.id.contenedor_pequeno) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contenedor_pequeno, selectorFragment).commit();
        }

        if(getIntent().getExtras()!=null) {
            Log.d("NotApp", "onCreate: Se inició con Intent: " + getIntent().getIntExtra("rep", 0));
            mostrarDetallenotificacion(getIntent().getIntExtra("rep", 0));
        } else {
            Log.d("NotApp", "onCreate: Se inició sin Intent");
        }
    }

    public void mostrarDetalle(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.detalle_fragment) != null){
            DetalleFragment fragment_detalle = (DetalleFragment) fragmentManager.findFragmentById(R.id.detalle_fragment);
            fragment_detalle.ponInfoLibro(index);
        } else {
            DetalleFragment detalleFragment = new DetalleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(DetalleFragment.ARG_ID_LIBRO, index);
            detalleFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.contenedor_pequeno, detalleFragment).addToBackStack(null).commit();
        }
    }

    public void mostrarDetallenotificacion(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetalleFragment detalleFragment = new DetalleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DetalleFragment.ARG_ID_LIBRO, index);
        bundle.putBoolean("Service", true);
        detalleFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.contenedor_pequeno
                , detalleFragment).addToBackStack(null).commit();
    }
}