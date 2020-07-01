package com.mygdx.game;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import retrofit2.Response;


public class ShareFB extends AppCompatActivity {

    private ShareDialog shareDialog;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //share();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://monster-game-db.000webhostapp.com/monster.png"))
                    .setQuote("I just played Monsters. My score was: " + String.valueOf(fetchScorefromLibGDX()))
                    .build();

            shareDialog.show(linkContent);
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private int fetchScorefromLibGDX() {

        String score = " ";
        try {
            Preferences prefs = Gdx.app.getPreferences("prefs");
            score = prefs.getString("score", "No name stored");
            if(!score.equals("No name stored")) {
                return Integer.parseInt(score);
            }
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
    public void share(){

        String score = String.valueOf(fetchScorefromLibGDX());
        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "game.achievement")
                .putString("og:title", "MONSTER\nMy highest score is "+  " " + score)
                .build();


        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("games.achieves")
                .putObject("game", object)
                .build();




        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("game")
                    .setAction(action)
                    .build();

            ShareDialog.show(ShareFB.this, content);
        }

    }


}
