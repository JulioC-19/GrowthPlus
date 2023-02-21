package com.GrowthPlus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.GrowthPlus.customViews.ChildAvatarComponent;
import com.GrowthPlus.dataAccessLayer.ChildRoadMap.ChildRoadMap;
import com.GrowthPlus.dataAccessLayer.Language.LanguageSchema;
import com.GrowthPlus.dataAccessLayer.Language.LanguageSchemaService;
import com.GrowthPlus.dataAccessLayer.RoadMapLesson.RoadMapLesson;
import com.GrowthPlus.dataAccessLayer.RoadMapQuiz.RoadMapQuiz;
import com.GrowthPlus.dataAccessLayer.RoadMapScenarioGame.RoadMapScenarioGame;
import com.GrowthPlus.dataAccessLayer.child.ChildSchema;
import com.GrowthPlus.dataAccessLayer.child.ChildSchemaService;
import com.GrowthPlus.dataAccessLayer.parent.ParentSchema;
import com.GrowthPlus.dataAccessLayer.parent.ParentSchemaService;
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
    ParentSchemaService parentService;
    Realm realm;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();

        // ROADMAP 1 : lessons, quizzes, and game

        RoadMapLesson roadMapLesson1 = new RoadMapLesson(
                "Roadmap One Lesson 1",
                "elephant",
                "numbers",
                false,
                true,
                7,
                10,
                "RmOneLessonOne",
                "RmOneLessonOneContentOne",
                "RmOneLessonOneFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson2 = new RoadMapLesson(
                "Roadmap One Lesson 2",
                "elephant",
                "addition",
                false,
                true,
                7,
                10,
                "RmOneLessonTwo",
                "RmOneLessonTwoContentOne",
                "RmOneLessonTwoFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson3 = new RoadMapLesson(
                "Roadmap One Lesson 3",
                "unit",
                "units",
                false,
                true,
                7,
                10,
                "RmOneLessonThree",
                "RmOneLessonThreeContentOne",
                "RmOneLessonThreeFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson4 = new RoadMapLesson(
                "Roadmap One Lesson 4",
                "elephant",
                "numbers",
                false,
                true,
                7,
                10,
                "RmOneLessonFour",
                "RmOneLessonFourContentOne",
                "RmOneLessonFourFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson5 = new RoadMapLesson(
                "Roadmap One Lesson 5",
                "elephant",
                "subtraction",
                false,
                true,
                7,
                10,
                "RmOneLessonFive",
                "RmOneLessonFiveContentOne",
                "RmOneLessonFiveFlashFive",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson6 = new RoadMapLesson(
                "Roadmap One Lesson 6",
                "elephant",
                "multiplication",
                false,
                true,
                7,
                10,
                "RmOneLessonSix",
                "RmOneLessonSixContentOne",
                "RmOneLessonSixFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson7 = new RoadMapLesson(
                "Roadmap One Lesson 7",
                "elephant",
                "division",
                false,
                true,
                7,
                10,
                "RmOneLessonSeven",
                "RmOneLessonSevenContentOne",
                "RmOneLessonSevenFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson8 = new RoadMapLesson(
                "Roadmap One Lesson 8",
                "elephant",
                "multiplication",
                false,
                true,
                7,
                10,
                "RmOneLessonEight",
                "RmOneLessonEightContentOne",
                "RmOneLessonEightFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson9 = new RoadMapLesson(
                "Roadmap One Lesson 9",
                "elephant",
                "division",
                false,
                true,
                7,
                10,
                "RmOneLessonNine",
                "RmOneLessonNineContentOne",
                "RmOneLessonNineFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson10 = new RoadMapLesson(
                "Roadmap One Lesson 10",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmOneLessonTen",
                null,
                "RmOneLessonOneFlashOne",
                true,
                false,
                0);
        RealmList<RoadMapLesson> roadMapLessons = new RealmList<>();
        roadMapLessons.add(roadMapLesson1);
        roadMapLessons.add(roadMapLesson2);
        roadMapLessons.add(roadMapLesson3);
        roadMapLessons.add(roadMapLesson4);
        roadMapLessons.add(roadMapLesson5);
        roadMapLessons.add(roadMapLesson6);
        roadMapLessons.add(roadMapLesson7);
        roadMapLessons.add(roadMapLesson8);
        roadMapLessons.add(roadMapLesson9);
        roadMapLessons.add(roadMapLesson10);

        RoadMapQuiz roadMapQuiz1 = new RoadMapQuiz(
                "RoadMap One Quiz One",
                "elephant",
                10,
                7,
                false,
                true,
                "RmOneQuizOne",
                0
        );
        RoadMapQuiz roadMapQuiz2 = new RoadMapQuiz(
                "RoadMap One Quiz Two",
                "elephant",
                10,
                7,
                false,
                true,
                "RmOneQuizTwo",
                0
        );
        RealmList<RoadMapQuiz> roadMapQuizzes = new RealmList<>();
        roadMapQuizzes.add(roadMapQuiz1);
        roadMapQuizzes.add(roadMapQuiz2);

        RoadMapScenarioGame roadMapOneScenarioGame = new RoadMapScenarioGame(
                "Game 1",
                "elephant",
                20,
                17,
                false,
                "RmOneScenarioGame",
                0
        );

        // ROADMAP 2 : lessons, quizzes, and game

        RoadMapLesson roadMapLesson11 = new RoadMapLesson(
                "Roadmap Two Lesson 1",
                "camel",
                "numbers",
                false,
                true,
                7,
                10,
                "RmTwoLessonOne",
                "RmTwoLessonOneContentOne",
                "RmTwoLessonOneFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson12 = new RoadMapLesson(
                "Roadmap Two Lesson 2",
                "camel",
                "addition",
                false,
                true,
                7,
                10,
                "RmTwoLessonTwo",
                "RmTwoLessonTwoContentOne",
                "RmTwoLessonTwoFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson13 = new RoadMapLesson(
                "Roadmap Two Lesson 3",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonThree",
                "RmTwoLessonThreeContentOne",
                "RmTwoLessonThreeFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson14 = new RoadMapLesson(
                "Roadmap Two Lesson 4",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonFour",
                "RmTwoLessonFourContentOne",
                "RmTwoLessonFourFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson15 = new RoadMapLesson(
                "Roadmap Two Lesson 5",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonFive",
                "RmTwoLessonFiveContentOne",
                "RmTwoLessonFiveFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson16 = new RoadMapLesson(
                "Roadmap Two Lesson 6",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonSix",
                "RmTwoLessonSixContentOne",
                "RmTwoLessonSixFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson17 = new RoadMapLesson(
                "Roadmap Two Lesson 7",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonSeven",
                "RmTwoLessonSevenContentOne",
                "RmTwoLessonSevenFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson18 = new RoadMapLesson(
                "Roadmap Two Lesson 8",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonEight",
                "RmTwoLessonEightContentOne",
                "RmTwoLessonEightFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson19 = new RoadMapLesson(
                "Roadmap Two Lesson 9",
                null,
                null,
                false,
                true,
                7,
                10,
                "RmTwoLessonNine",
                "RmTwoLessonNineContentOne",
                "RmTwoLessonNineFlashOne",
                true,
                false,
                0);
        RoadMapLesson roadMapLesson20 = new RoadMapLesson(
                "Roadmap Two Lesson 10",
                null,
                null,
                true,
                false,
                7,
                10,
                "RmTwoLessonTen",
                "RmTwoLessonTenContentOne",
                "RmTwoLessonTenFlashOne",
                true,
                false,
                0);
        RealmList<RoadMapLesson> roadMapLessons2 = new RealmList<>();
        roadMapLessons2.add(roadMapLesson11);
        roadMapLessons2.add(roadMapLesson12);
        roadMapLessons2.add(roadMapLesson13);
        roadMapLessons2.add(roadMapLesson14);
        roadMapLessons2.add(roadMapLesson15);
        roadMapLessons2.add(roadMapLesson16);
        roadMapLessons2.add(roadMapLesson17);
        roadMapLessons2.add(roadMapLesson18);
        roadMapLessons2.add(roadMapLesson19);
        roadMapLessons2.add(roadMapLesson20);

        RoadMapQuiz roadMapQuiz3 = new RoadMapQuiz(
                "RoadMap Two Quiz One",
                "camel",
                10,
                7,
                false,
                true,
                "RmTwoQuizOne",
                0
        );
        RealmList<RoadMapQuiz> roadMapQuizzes2 = new RealmList<>();
        roadMapQuizzes2.add(roadMapQuiz3);

        // EMBEDDED CHILD ROADMAPS

        ChildRoadMap childRoadMapOne = new ChildRoadMap(
                "roadMapOne",
                9,
                false,
                true,
                false,
                roadMapLessons,
                roadMapQuizzes,
                roadMapOneScenarioGame,
                "RoadMapOne");

        ChildRoadMap childRoadMapTwo = new ChildRoadMap(
                "roadMapTwo",
                9,
                true,
                false,
                false,
                roadMapLessons2,
                roadMapQuizzes2,
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
            if (nameInput.getText().toString() != null && !nameInput.getText().toString().equals("")){
                newChild = new ChildSchemaService(realm,
                        nameInput.getText().toString(),
                        animalName,
                        colorName, 0,
                        childRoadMapOne,
                        childRoadMapTwo,
                        childRoadMapThree,
                        childRoadMapFour);

                ObjectId childId = new ObjectId();
                newChild.createChildSchema(String.valueOf(childId));

                // Adding newly created child to parent's children
                realm.executeTransactionAsync(realm -> {
                    ChildSchema child = realm.where(ChildSchema.class).equalTo("childId", childId.toString()).findFirst();
                    ParentSchema parent = realm.where(ParentSchema.class).findFirst();
                    parent.getChildren().add(child);

                }, ()->{
                        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }, error -> {
                        Log.i("Error", "Could not add child to parent " + error);
                });

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
        resources = getResources();
        backButton = findViewById(R.id.backCreateAccount);
        loginButton = findViewById(R.id.loginBtn);
        nameInput = findViewById(R.id.nameInput);
        colorIdentifier = new ColorIdentifier();
        imageSrcIdentifier = new ImageSrcIdentifier();
        childAvatar = findViewById(R.id.childAvatar);
        parentService = new ParentSchemaService(realm);

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

    @Override
    protected void onResume() {
        super.onResume();

        // create instance of shared preferences
        SharedPreferences langPrefs = getSharedPreferences("LangPreferences", MODE_PRIVATE);
        // create language schema service and set strings
        LanguageSchemaService langSchemaService = new LanguageSchemaService(realm, langPrefs.getString("languageId", "frenchZero"));
        LanguageSchema lang = langSchemaService.getLanguageSchemaById();

        nameInput.setHint(lang.getName());

    }

    // Change custom view animal and color
    public void setChildAvatar(){
        childAvatar.setImageResource(imageSrcIdentifier.getImageSrcId(animalName));
        childAvatar.setBackgroundTintList(color);
    }
}