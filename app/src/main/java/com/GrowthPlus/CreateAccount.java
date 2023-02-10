package com.GrowthPlus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.GrowthPlus.customViews.ChildAvatarComponent;
import com.GrowthPlus.dataAccessLayer.ChildRoadMap.ChildRoadMap;
import com.GrowthPlus.dataAccessLayer.RoadMapLesson.RoadMapLesson;
import com.GrowthPlus.dataAccessLayer.child.ChildSchemaService;
import com.GrowthPlus.utilities.ColorIdentifier;
import com.GrowthPlus.utilities.ImageSrcIdentifier;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmList;

public class CreateAccount extends AppCompatActivity {
    Button backButton, loginButton;
    EditText nameInput;
    ColorIdentifier colorIdentifier;
    ImageSrcIdentifier imageSrcIdentifier;
    String colorName, animalName;
    ChildAvatarComponent childAvatar;
    ColorStateList color;
    ChildSchemaService newChild;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();

        RoadMapLesson roadMapLesson = new RoadMapLesson(
                "Roadmap One Lesson 1",
                "elephant",
                "numbers",
                true,
                false,
                7,
                10,
                "RmOneLessonOne",
                "RmOneLessonOneContentOne",
                "RmOneLessonOneFlashOne",
                true,
                false,
                0);
        RealmList<RoadMapLesson> roadMapLessons = new RealmList<>();
        roadMapLessons.add(roadMapLesson);


        ChildRoadMap childRoadMapOne = new ChildRoadMap(
                "roadMapOne",
                0,
                true,
                false,
                false,
                roadMapLessons,
                null,
                null,
                "RoadMapOne");

        ChildRoadMap childRoadMapTwo = new ChildRoadMap(
                "roadMapTwo",
                0,
                false,
                false,
                true,
                null,
                null,
                null,
                "RoadMapTwo");

        ChildRoadMap childRoadMapThree = new ChildRoadMap(
                "roadMapThree",
                0,
                false,
                false,
                true,
                null,
                null,
                null,
                "RoadMapThree");

        ChildRoadMap childRoadMapFour = new ChildRoadMap(
                "roadMapFour",
                0,
                false,
                false,
                true,
                null,
                null,
                null,
                "RoadMapFour");

        // Go to main page with update new child
        View.OnClickListener goNext = v -> {
            if (!nameInput.getText().toString().equals(null) && !nameInput.getText().toString().equals("")){

                newChild = new ChildSchemaService(realm,
                        nameInput.getText().toString(),
                        animalName,
                        colorName, 0,
                        childRoadMapOne,
                        childRoadMapTwo,
                        childRoadMapThree,
                        childRoadMapFour);

                ObjectId childId = new ObjectId();
                newChild.createChildSchema(String.valueOf(childId)); // Create new child in realm database
                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        };
        loginButton.setOnClickListener(goNext);

        // Go back to select child avatar
        View.OnClickListener goBack = v -> {
            finish();
        };
        backButton.setOnClickListener(goBack);
    }

    public void init(){
        realm = Realm.getDefaultInstance();
        backButton = findViewById(R.id.backCreateAccount);
        loginButton = findViewById(R.id.loginBtn);
        nameInput = findViewById(R.id.nameInput);
        colorIdentifier = new ColorIdentifier();
        imageSrcIdentifier = new ImageSrcIdentifier();
        childAvatar = findViewById(R.id.childAvatar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            colorName = extras.getString("selectColor"); // Get color
            animalName = extras.getString("selectAnimal"); // Get animal
        }
        color = ContextCompat.getColorStateList(this, colorIdentifier.getColorIdentifier(colorName));

        backButton.setBackgroundTintList(color); // Set color
        loginButton.setBackgroundTintList(color);

        setChildAvatar();
    }

    // Change custom view animal and color
    public void setChildAvatar(){
        childAvatar.setImageResource(imageSrcIdentifier.getImageSrcId(animalName));
        childAvatar.setBackgroundTintList(color);
    }
}