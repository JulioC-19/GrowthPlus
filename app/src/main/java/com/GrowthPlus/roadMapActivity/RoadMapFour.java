package com.GrowthPlus.roadMapActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.GrowthPlus.ChildPortal;
import com.GrowthPlus.IntroScreen;
import com.GrowthPlus.Lesson4;
import com.GrowthPlus.R;
import com.GrowthPlus.customViews.RoadMapLessonTrail;
import com.GrowthPlus.customViews.RoadMapTile;
import com.GrowthPlus.customViews.TopBar;
import com.GrowthPlus.dataAccessLayer.ChildRoadMap.ChildRoadMap;
import com.GrowthPlus.dataAccessLayer.RoadMapLesson.RoadMapLesson;
import com.GrowthPlus.dataAccessLayer.RoadMapScenarioGame.RoadMapScenarioGame;
import com.GrowthPlus.dataAccessLayer.child.ChildSchema;
import com.GrowthPlus.dataAccessLayer.child.ChildSchemaService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.GrowthPlus.dataAccessLayer.RoadMapQuiz.RoadMapQuiz;

import java.util.HashMap;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;

public class RoadMapFour extends AppCompatActivity implements View.OnClickListener{
    Button goBackButton;
    BottomNavigationView bottomNavigationView;
    ConstraintLayout roadMapFour;
    RoadMapLessonTrail roadMapFourLessonTrail;
    TopBar topBarFour;
    Realm realm;
    ChildSchemaService childSchemaService;
    String childID;
    RoadMapTile tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13;
    Intent IntentIntro; // Leads to location_intro page
    ChildRoadMap childRoadMapFour;
    Integer lessonCompleted;
    RealmList<RoadMapLesson> roadMapLessons;
    HashMap<Integer, RoadMapTile> mapTiles;
    HashMap<Integer, String> mapLessonId;
    RealmList<RoadMapQuiz> roadMapQuizes;
    RoadMapScenarioGame game;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_map_four);

        realm = Realm.getDefaultInstance();
        childSchemaService = new ChildSchemaService(realm);

        // !!! MUST PASS CHILD.ID WITH 'putExtra' WHEN NAVIGATING BETWEEN LEVELS OR ELSE SYSTEM CRASH !!!
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            childID = extras.getString("childIdentify");
        }
        ChildSchema child = childSchemaService.getChildSchemaById(childID);
        init(child);

        if(child.getRoadMapFour().getLocked()){
            initState();
        }else{
            roadMapFourLessonTrail.unLockRoadMap();
            setLessonTiles(child);
        }

        bottomNavigationView.setSelectedItemId(R.id.roadMap4item);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.roadMap1item: {
                    Intent intent = new Intent(getApplicationContext(), RoadMapOne.class);
                    intent.putExtra("childIdentify", childID);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                case R.id.roadMap2item: {
                    Intent intent = new Intent(getApplicationContext(), RoadMapTwo.class);
                    intent.putExtra("childIdentify", childID);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                case R.id.roadMap3item: {
                    Intent intent = new Intent(getApplicationContext(), RoadMapThree.class);
                    intent.putExtra("childIdentify", childID);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                case R.id.roadMap4item:
                    return true;
            }
            return false;
        });

        goBackButton.setOnClickListener(this);
    }

    private void init(ChildSchema child){
        bottomNavigationView = findViewById(R.id.roadMapBottomNavigationView);
        topBarFour = findViewById(R.id.topBarFour);
        topBarFour.setPoints(String.valueOf(child.getScore()));
        goBackButton = topBarFour.findViewById(R.id.goBackBtn);
        roadMapFourLessonTrail = findViewById(R.id.roadMapFourLessonTrail);

        childRoadMapFour = child.getRoadMapFour();
        lessonCompleted = childRoadMapFour.getLessonsCompleted();
        roadMapLessons = childRoadMapFour.getRoadMapLessons();
        roadMapQuizes = childRoadMapFour.getRoadMapQuizzes();
        game = childRoadMapFour.getScenarioGame();

        tile1 = roadMapFourLessonTrail.getRoadMapTile1();
        tile2 = roadMapFourLessonTrail.getRoadMapTile2();
        tile3 = roadMapFourLessonTrail.getRoadMapTile3();
        tile4 = roadMapFourLessonTrail.getRoadMapTile4();
        tile5 = roadMapFourLessonTrail.getRoadMapTile5();
        tile6 = roadMapFourLessonTrail.getRoadMapTile6();
        tile7 = roadMapFourLessonTrail.getRoadMapTile7();
        tile8 = roadMapFourLessonTrail.getRoadMapTile8();
        tile9 = roadMapFourLessonTrail.getRoadMapTile9();
        tile10 = roadMapFourLessonTrail.getRoadMapTile10();
        tile11 = roadMapFourLessonTrail.getRoadMapTile11();
        tile12 = roadMapFourLessonTrail.getRoadMapTile12();
        tile13 = roadMapFourLessonTrail.getRoadMapTile13();

        mapTiles = new HashMap<>();
        mapLessonId = new HashMap<>();
        mapRoadMapTiles();

    }

    private void mapRoadMapTiles(){
        mapTiles.put(0, tile1);
        mapTiles.put(1, tile2);
        mapTiles.put(2, tile3);
        mapTiles.put(3, tile5);
        mapTiles.put(4, tile6);
        mapTiles.put(5, tile7);
        mapTiles.put(6, tile8);
        mapTiles.put(7, tile10);
        mapTiles.put(8, tile11);
        mapTiles.put(9, tile12);

    }

    private void setLessonTiles(ChildSchema child){
        for(int i = 0; i <= lessonCompleted; i++){ // Loop thru the lessons completed
            RoadMapLesson roadMapLessonTemp = roadMapLessons.get(i);
            assert roadMapLessonTemp != null;

            String dataBaseLessonId = roadMapLessonTemp.getDatabaseLessonId();
            Integer tileIdTemp = Objects.requireNonNull(mapTiles.get(i)).getId();

            if(roadMapLessonTemp.getCompleted()){
                Objects.requireNonNull(mapTiles.get(i)).setCompletedState();
            }

            if(roadMapLessonTemp.getCurrent()){
                roadMapFourLessonTrail.setSelectedState(Objects.requireNonNull(mapTiles.get(i)), child);
            }

            Objects.requireNonNull(mapTiles.get(i)).setOnClickListener(this);
            mapLessonId.put(tileIdTemp, dataBaseLessonId);
        }

        tile4.setOnClickListener(this);
        tile4.setEnabled(false);
        if(roadMapQuizes.get(0).getCompleted()){
            tile4.setCompletedState();
            tile4.setEnabled(true);
        }
        if(roadMapQuizes.get(0).getCurrent()){
            roadMapFourLessonTrail.setSelectedState(tile4, child);
            tile4.setEnabled(true);
        }

        tile9.setOnClickListener(this);
        tile9.setEnabled(false);
        if(roadMapQuizes.get(1).getCompleted()){
            tile9.setCompletedState();
            tile9.setEnabled(true);
        }
        if(roadMapQuizes.get(1).getCurrent()){
            roadMapFourLessonTrail.setSelectedState(tile9, child);
            tile9.setEnabled(true);
        }

        tile13.setOnClickListener(this);
        tile13.setEnabled(false);
        if(game.getCompleted()){
            tile13.setCompletedState();
            tile13.setEnabled(true);
        }
        if(game.getCurrent()){
            roadMapFourLessonTrail.setSelectedState(tile13, child);
            tile13.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(buttonClick);
        int viewId = view.getId();

        if(viewId == goBackButton.getId()){
            Intent childPortal = new Intent(RoadMapFour.this, ChildPortal.class);
            childPortal.putExtra("childIdLandingPage", childID);
            startActivity(childPortal);
            this.finish();
        }

        else if(viewId == tile1.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 0);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile2.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 1);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile3.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 2);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile4.getId()){
            IntentIntro = new Intent(RoadMapFour.this, IntroScreen.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("databaseQuizId", roadMapQuizes.get(0).getDatabaseQuizId());
            IntentIntro.putExtra("whichOne", "Quiz");
            IntentIntro.putExtra("quizIndex", 0);
            IntentIntro.putExtra("whichRoadMap", "4");
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile5.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 3);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile6.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 4);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile7.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 5);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile8.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 6);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile9.getId()){
            IntentIntro = new Intent(RoadMapFour.this, IntroScreen.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("databaseQuizId", roadMapQuizes.get(1).getDatabaseQuizId());
            IntentIntro.putExtra("whichOne", "Quiz");
            IntentIntro.putExtra("quizIndex", 1);
            IntentIntro.putExtra("whichRoadMap", "4");
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile10.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 7);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile11.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 8);
            startActivity(IntentIntro);
            this.finish();
        }

        else if(viewId == tile12.getId()){
            IntentIntro = new Intent(RoadMapFour.this, Lesson4.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("dataBaseLessonId", mapLessonId.get(viewId));
            IntentIntro.putExtra("lessonIndex", 9);
            startActivity(IntentIntro);
            this.finish();
        }


        else if(viewId == tile13.getId()){
            IntentIntro = new Intent(RoadMapFour.this, IntroScreen.class);
            IntentIntro.putExtra("childId", childID);
            IntentIntro.putExtra("databaseQuizId", game.getDatabaseScenarioGameId());
            IntentIntro.putExtra("whichOne", "Game");
            IntentIntro.putExtra("whichRoadMap", "4");
            startActivity(IntentIntro);
            this.finish();
        }
    }

    // Initial state is locked. Check if previous 3 levels completed before unlocking
    private void initState(){
        roadMapFourLessonTrail.setAlpha(.7f);
    }
}