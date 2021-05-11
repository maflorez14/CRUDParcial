package app.crudparcial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.crudparcial.model.Persona;

public class MainActivity extends AppCompatActivity {

    private List<Persona> personaList = new ArrayList<Persona>();
    ArrayAdapter<Persona> ArrayAdapterPersona;

    Persona personaSelected;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText nomPersona, idPersona;
    String genPersona;
    RadioButton opc1, opc2, opc3;
    ListView listPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomPersona = findViewById(R.id.txt_nombrePersona);
        idPersona = findViewById(R.id.txt_idPersona);
        opc1 = (RadioButton) findViewById(R.id.radioButton1);
        opc2 = (RadioButton) findViewById(R.id.radioButton2);
        opc3 = (RadioButton) findViewById(R.id.radioButton3);
        listPersona = findViewById(R.id.ListView);
        
        conFirebase();
        listarDatos();
        listPersona.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String genero = "";
                //limpiarCajas();

                personaSelected = (Persona) adapterView.getItemAtPosition(position);

                nomPersona.setText(personaSelected.getNombre());
                idPersona.setText(personaSelected.getCodigo());

                genero = personaSelected.getGenero().toString();
                
                getGeneroPer(genero);
            }
        });
    }

    private void getGeneroPer(String gene) {

        if(gene.equals("Masculino")){
            opc1.setChecked(true);
        }else if(gene.equals("Femenino")){
            opc2.setChecked(true);
        }else{
            opc3.setChecked(true);
        }
    }

    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personaList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    personaList.add(p);

                    ArrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, personaList);
                    listPersona.setAdapter(ArrayAdapterPersona);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void conFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nomPersona.getText().toString();
        String codigo = idPersona.getText().toString();

        switch(item.getItemId()) {
            case R.id.icon_add: {
                if(nombre.equals("")||codigo.equals("")){
                    validacion();
                }
                else{
                    Radio();
                    Persona p = new Persona();
                    p.setNombre(nombre);
                    p.setCodigo(codigo);
                    p.setId(UUID.randomUUID().toString());
                    p.setGenero(genPersona);
                    databaseReference.child("Persona").child(p.getId()).setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_delete: {

                Persona p = new Persona();
                p.setId(personaSelected.getId());
                databaseReference.child("Persona").child(p.getId()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            case R.id.icon_update: {

                Radio();
                Persona p = new Persona();
                p.setId(personaSelected.getId());
                p.setCodigo(codigo);
                p.setNombre(nomPersona.getText().toString().trim());
                p.setGenero(genPersona);
                databaseReference.child("Persona").child(p.getId()).setValue(p);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return true;
    }

    private void limpiarCajas() {

        nomPersona.setText("");
        idPersona.setText("");
        opc2.setChecked(false);
        opc3.setChecked(false);
        opc1.setChecked(false);
    }

    private void validacion() {

        String nombre = nomPersona.getText().toString();
        String idPer = idPersona.getText().toString();

        if(nombre.equals("")){
            nomPersona.setError("Ingrese el campo vacio");
        }
        if(idPer.equals("")){
            idPersona.setError("Ingrese el campo vacio");
        }
    }

    private void Radio(){

        if(opc1.isChecked()==true){
            genPersona= "Masculino";
        } else if(opc2.isChecked()){
            genPersona= "Femenino";
        } else if(opc3.isChecked()){
            genPersona= "Otro";
        } else{
            opc1.setError("Selecciona una opccion");
        }
    }
}