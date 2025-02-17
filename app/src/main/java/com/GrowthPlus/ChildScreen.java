package com.GrowthPlus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.GrowthPlus.customViews.ChildAvatarComponent;
import com.GrowthPlus.customViews.ChildNameScoreComponent;
import com.GrowthPlus.customViews.HorizontalProgressBar;
import com.GrowthPlus.customViews.SubjectCompletionComponent;
import com.GrowthPlus.dataAccessLayer.Language.Translator;
import com.GrowthPlus.dataAccessLayer.RoadMapLesson.RoadMapLesson;
import com.GrowthPlus.dataAccessLayer.child.ChildSchema;
import com.GrowthPlus.dataAccessLayer.child.ChildSchemaService;
import com.GrowthPlus.utilities.ColorIdentifier;
import com.GrowthPlus.utilities.ImageSrcIdentifier;

import io.realm.Realm;

public class ChildScreen extends AppCompatActivity {
    private Button backParentPortal;
    private Button deleteChildButton;
    private ChildAvatarComponent childAvatar;
    private ChildNameScoreComponent childNameAndScore;

    private HorizontalProgressBar horizontalProgressBarOne;
    private HorizontalProgressBar horizontalProgressBarTwo;
    private HorizontalProgressBar horizontalProgressBarThree;
    private HorizontalProgressBar horizontalProgressBarFour;

    private SubjectCompletionComponent numbers;
    private SubjectCompletionComponent length;
    private SubjectCompletionComponent unit;
    private SubjectCompletionComponent weight;
    private SubjectCompletionComponent addition;
    private SubjectCompletionComponent money;
    private SubjectCompletionComponent subtraction;
    private SubjectCompletionComponent time;
    private SubjectCompletionComponent multiplication;
    private SubjectCompletionComponent shapes;
    private SubjectCompletionComponent division;
    private SubjectCompletionComponent angles;
    private ColorIdentifier colorIdentifier;
    private ImageSrcIdentifier imageSrcIdentifier;
    private String childId;
    private ColorStateList progressBarOneColor;
    private ColorStateList progressBarTwoColor;
    private ColorStateList progressBarThreeColor;
    private ColorStateList progressBarFourColor;
    private Realm realm;
    private ChildSchemaService childSchemaService;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Translator translator;
    private AlertDialog dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_screen);
        init();

        // Create instance of shared preferences and save current language id
        SharedPreferences langPrefs = getSharedPreferences("LangPreferences", MODE_PRIVATE);
        String langId = langPrefs.getString("languageId", "frenchZero");
        // Create language translator and set up the Lesson string
        translator = new Translator(langId);

        ChildSchema child = childSchemaService.getChildSchemaById(childId);
        setChildMetaData(child);

        RoadMapLesson lastLesson = child.getRoadMapOne().getRoadMapLessons().last();
        Integer lessonsCompleted = roadMapLessonProgress(lastLesson, child.getRoadMapOne().getLessonsCompleted());

        setProgressBar(
                horizontalProgressBarOne,
                "1",
                progressBarOneColor,
                lessonsCompleted
        );

        lastLesson = child.getRoadMapTwo().getRoadMapLessons().last();
        lessonsCompleted = roadMapLessonProgress(lastLesson, child.getRoadMapTwo().getLessonsCompleted());
        setProgressBar(
                horizontalProgressBarTwo,
                "2",
                progressBarTwoColor,
                lessonsCompleted
        );

        lastLesson = child.getRoadMapThree().getRoadMapLessons().last();
        lessonsCompleted = roadMapLessonProgress(lastLesson, child.getRoadMapThree().getLessonsCompleted());
        setProgressBar(
                horizontalProgressBarThree,
                "3",
                progressBarThreeColor,
                lessonsCompleted
        );

        lastLesson = child.getRoadMapFour().getRoadMapLessons().last();
        lessonsCompleted = roadMapLessonProgress(lastLesson, child.getRoadMapFour().getLessonsCompleted());
        setProgressBar(
                horizontalProgressBarFour,
                "4",
                progressBarFourColor,
                lessonsCompleted
        );

        setSubjectsCompletion(child);

        backParentPortal.setOnClickListener(view -> {
            view.startAnimation(buttonClick);
            startActivity(new Intent(ChildScreen.this, ParentPortal.class));
            this.finish();
        });

        deleteChildButton.setOnClickListener(view -> {
            view.startAnimation(buttonClick);
            createDeleteChildDialogue(child);
        });

    }

    private void init(){
        realm = Realm.getDefaultInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            childId = extras.getString("childIdParentPortal");
        }

        childSchemaService = new ChildSchemaService(realm);

        backParentPortal = findViewById(R.id.backParentPortal);
        deleteChildButton = findViewById(R.id.deleteChildButton);
        childAvatar = findViewById(R.id.childAvatar);
        childNameAndScore = findViewById(R.id.childNameAndScore);

        horizontalProgressBarOne = findViewById(R.id.horizontalProgressBarLevelOne);
        horizontalProgressBarTwo = findViewById(R.id.horizontalProgressBarLevelTwo);
        horizontalProgressBarThree = findViewById(R.id.horizontalProgressBarLevelThree);
        horizontalProgressBarFour = findViewById(R.id.horizontalProgressBarLevelFour);

        progressBarOneColor = ContextCompat.getColorStateList(this, R.color.light_green);
        progressBarTwoColor = ContextCompat.getColorStateList(this, R.color.orange);
        progressBarThreeColor = ContextCompat.getColorStateList(this, R.color.blue);
        progressBarFourColor = ContextCompat.getColorStateList(this, R.color.yellow);

        numbers = findViewById(R.id.numbersCompletion);
        length = findViewById(R.id.lengthCompletion);
        unit = findViewById(R.id.unitCompletion);
        weight = findViewById(R.id.weightCompletion);
        addition = findViewById(R.id.additionCompletion);
        money = findViewById(R.id.moneyCompletion);
        subtraction = findViewById(R.id.subtractionCompletion);
        time = findViewById(R.id.timeCompletion);
        multiplication = findViewById(R.id.multiplicationCompletion);
        shapes = findViewById(R.id.shapesCompletion);
        division = findViewById(R.id.divisionCompletion);
        angles = findViewById(R.id.anglesCompletion);

        colorIdentifier = new ColorIdentifier();
        imageSrcIdentifier = new ImageSrcIdentifier();
    }

    @Override
    protected void onDestroy() {
        if (!realm.isClosed()) realm.close();
        super.onDestroy();
    }

    private Integer roadMapLessonProgress(RoadMapLesson lastLesson, Integer lessons){
        int lessonsCompleted = lessons == 9 && lastLesson.getCompleted() ? lessons + 1 : lessons;
        int MAX_LESSONS_RM = 10;
        double percentage = ((double) lessonsCompleted / (double) MAX_LESSONS_RM);
        double progress = percentage * 100;

        return (int) progress;
    }

    private void setProgressBar(HorizontalProgressBar temp,  CharSequence text, ColorStateList tint, Integer progress){
        temp.setBarLevelText(text);
        temp.setBarLevelColor(tint);
        temp.setHorizontalBarColor(tint);
        temp.setHorizontalBarProgress(progress);
    }

    private void setChildAvatar(String childAvatarName, ColorStateList color){
        childAvatar.setBackgroundTintList(color);
        childAvatar.setImageResource(imageSrcIdentifier.getImageSrcId(childAvatarName));
    }

    private void setChildNameAndScore(String childName, Integer score){
        childNameAndScore.setChildNameText(childName);
        childNameAndScore.setChildScoreText(String.valueOf(score));
    }

    private void setChildMetaData(ChildSchema child){
        ColorStateList color = ContextCompat.getColorStateList(this, colorIdentifier.getColorIdentifier(child.getColorName()));
        String name = child.getName();
        String avatarName = child.getAvatarName();

        Integer score = child.getScore();
        setChildAvatar(avatarName, color);
        setChildNameAndScore(name, score);
    }

    //this creates the delete child popup
    public void createDeleteChildDialogue(ChildSchema deleteChild){
        //create the dialogue builder using this context
        //this is for the delete child confirmation popup screen
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);

        //to create the view we have an inflator that calls our custom xml file
        View deleteChildPopupView = getLayoutInflater().
                inflate(R.layout.delete_child_confirmation_popup,
                        null);

        //here we want to grab the confirm and cancel buttons from the view
        //this is important so that we can set up the proper logic for the
        //onClickListeners
        Button confirmChildDelete = deleteChildPopupView.findViewById(R.id.confirmBtn);
        Button cancelChildDelete = deleteChildPopupView.findViewById(R.id.cancelBtn);

        //here we grab the fields in the custom PopUp xml file that we want to change
        //based on the child that is being deleted
        TextView childNameDel = deleteChildPopupView.findViewById(R.id.childName);
        ImageView childAvatarDel = deleteChildPopupView.findViewById(R.id.childAvatar);
        TextView deleteText = deleteChildPopupView.findViewById(R.id.delete);


        //here we set the child name and avatar to the popUp so that the parent can
        //know which child they are or are not deleting
        childNameDel.setText(deleteChild.getName());
        childAvatarDel.setImageResource(imageSrcIdentifier.getImageSrcId(deleteChild.getAvatarName()));

        // Grab the current language selection and update the delete text with that language
        // Create instance of shared preferences and save current language id
        SharedPreferences langPrefs = getSharedPreferences("LangPreferences", MODE_PRIVATE);
        String langId = langPrefs.getString("languageId", "frenchZero");
        // Create language translator and set up the Lesson string
        Translator trans = new Translator(langId);
        String text = trans.getString("delete")+"?";
        deleteText.setText(text);

        //in the dialogue builder we have to set this view
        dialogueBuilder.setView(deleteChildPopupView);

        //now we create and build this view
        dialogue = dialogueBuilder.create();
        dialogue.show();

        //this is the logic if the parent confirms that they want to delete the child
        //we grab the child using their unique ID, delete the child, and verify that the
        //deletion was successful and eventually dismiss the popUp
        confirmChildDelete.setOnClickListener(view -> {
            realm.executeTransactionAsync(realm -> {
                ChildSchema childDel = realm.where(ChildSchema.class).equalTo("childId", childId).findFirst();
                assert childDel != null;
                childDel.deleteFromRealm();
            },()->{
                Intent intent = new Intent(ChildScreen.this, ParentPortal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(0, 0);
                startActivity(new Intent(ChildScreen.this, ParentPortal.class));
                overridePendingTransition(0, 0);
                this.finish();
            }, error -> Log.i("Error", "Could not delete child from realm " + error));
            dialogue.dismiss();
        });

        //here the parent does not wish to delete the child so we simply dismiss our popUp
        cancelChildDelete.setOnClickListener(v -> dialogue.dismiss());
    }

    private void setSubjectsCompletion(ChildSchema child){
        numbers.setSubjectCompletion(
                translator.getString("numbers"),
                6,
                child.getCatCountNumbers()
        );
        unit.setSubjectCompletion(
                translator.getString("unit"),
                2,
                child.getCatCountUnits()
        );
        addition.setSubjectCompletion(
                translator.getString("addition"),
                2,
                child.getCatCountAddition()
        );
        subtraction.setSubjectCompletion(
                translator.getString("subtraction"),
                2,
                child.getCatCountSubtraction()
        );
        multiplication.setSubjectCompletion(
                translator.getString("multiplication"),
                5,
                child.getCatCountMultiplication()
        );
        division.setSubjectCompletion(
                translator.getString("division"),
                5,
                child.getCatCountDivision()
        );
        length.setSubjectCompletion(
                translator.getString("length"),
                2,
                child.getCatCountLength()
        );
        weight.setSubjectCompletion(
                translator.getString("weights"),
                3,
                child.getCatCountWeightVolume()
        );
        money.setSubjectCompletion(
                translator.getString("money"),
                3,
                child.getCatCountMoney()
        );
        time.setSubjectCompletion(
                translator.getString("time"),
                1,
                child.getCatCountTime()
        );
        shapes.setSubjectCompletion(
                translator.getString("shapes"),
                1,
                child.getCatCountTime()
        );
        angles.setSubjectCompletion(
                translator.getString("angles"),
                2,
                child.getCatCountAngles()
        );
    }
}