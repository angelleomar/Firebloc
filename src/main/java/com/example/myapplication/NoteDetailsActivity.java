package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    LinearLayout cuadro;
    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn, colorBtn;
    MaterialCardView lista;
    MaterialButton btnCol1,btnCol2,btnCol3,btnCol4,btnCol5,btnCol6,btnCol7,btnCol8;
    TextView pageTitleTextView;
    String title,content,docId, color="null";
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        lista = findViewById(R.id.ccTrip);
        lista.setVisibility(View.GONE);

        cuadro = findViewById(R.id.cuadro);
        btnCol1 = findViewById(R.id.boton1);
        btnCol2 = findViewById(R.id.boton2);
        btnCol3 = findViewById(R.id.boton3);
        btnCol4 = findViewById(R.id.boton4);
        btnCol5 = findViewById(R.id.boton5);
        btnCol6 = findViewById(R.id.boton6);
        btnCol7 = findViewById(R.id.boton7);
        btnCol8 = findViewById(R.id.boton8);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        colorBtn = findViewById(R.id.deseign_btn);
        deleteNoteTextViewBtn  = findViewById(R.id.delete_note_text_view_btn);

        //receive data
        color = getIntent().getStringExtra("color");
        content= getIntent().getStringExtra("content");
        title = getIntent().getStringExtra("title");
        docId = getIntent().getStringExtra("docId");


        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Edita Tu Nota");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
           cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        }

        colorBtn.setOnClickListener((v)-> mostrarListaColores());

        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );

        btnCol1.setOnClickListener((v)-> colorBt1());
        btnCol2.setOnClickListener((v)-> colorBt2());
        btnCol3.setOnClickListener((v)-> colorBt3());
        btnCol4.setOnClickListener((v)-> colorBt4());
        btnCol5.setOnClickListener((v)-> colorBt5());
        btnCol6.setOnClickListener((v)-> colorBt6());
        btnCol7.setOnClickListener((v)-> colorBt7());
        btnCol8.setOnClickListener((v)-> colorBt8());
    }

    void mostrarListaColores(){
        if(lista.getVisibility() == View.GONE){
            Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            lista.startAnimation(slideUpAnimation);
            lista.setVisibility(View.VISIBLE);
        }
    }

    void colorBt1(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        color = "#FFFFFF";
    }

    void colorBt2(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC0CB")));
        color = "#FFC0CB";
    }

    void colorBt3(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFDAB9")));
        color = "#FFDAB9";
    }

    void colorBt4(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F0E68C")));
        color = "#F0E68C";
    }

    void colorBt5(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC0CB")));
        color = "#FFC0CB";
    }

    void colorBt6(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BA55D3")));
        color = "#BA55D3";
    }

    void colorBt7(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
        color = "#FFA500";
    }

    void colorBt8(){
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_salida);
        lista.startAnimation(slideUp);
        lista.setVisibility(View.GONE);
        cuadro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#90EE90")));
        color = "#90EE90";
    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String noteColor = color;

        if(color == null){
            noteColor = "#FFFFFF";
        }

        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Titulo es requerido");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        note.setColor(noteColor);
        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this,"Nota añadida con éxito");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Error al agregar la nota");
                }
            }
        });
    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Nota eliminada con éxito");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Error al eliminar la nota");
                }
            }
        });
    }


}
