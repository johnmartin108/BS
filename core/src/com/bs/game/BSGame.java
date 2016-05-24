package com.bs.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.Text;

import sun.rmi.runtime.Log;
//import com.bs.game.communication.*;

public class BSGame extends ApplicationAdapter {
	SpriteBatch batch;
	HashMap<Card, CardInfo> cards;
	private int numberSelected;
	private String currRank;
	private BitmapFont count;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		count = new BitmapFont();

		cards = new HashMap();
		numberSelected = 0;
		currRank = "aces";
		int width = Gdx.graphics.getWidth()/4;
		cards.put(new Card("h", "a"), new CardInfo(0, 0));
		cards.put(new Card("c", "a"), new CardInfo(1*width, 0));
		cards.put(new Card("d", "a"), new CardInfo(2*width, 0));
		cards.put(new Card("s", "a"), new CardInfo(3*width, 0));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		count.getData().setScale(10);
		count.draw(batch, "Play " + numberSelected + " " + currRank + "?", 750, 1000);
		batch.end();
		batch.begin();
		Iterator iter = cards.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mapPair = (Map.Entry) iter.next();
			Card card = (Card) mapPair.getKey();
			float x = cards.get(card).getX();
			float y = cards.get(card).getY();
			batch.draw(card.getTexture(), x, y);
		}
		batch.end();

		if(Gdx.input.justTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			System.out.println(Gdx.input.getX()+" "+Gdx.input.getY());

			clickInCard(touchPos);
		}

	}

	private int clickInCard(Vector3 touchPos) {
		Iterator iter = cards.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mapPair = (Map.Entry) iter.next();
			Card card = (Card) mapPair.getKey();
			CardInfo info = (CardInfo) mapPair.getValue();
			float x = info.getX();
			float y = info.getY();
			if (touchPos.x > info.getX() && touchPos.x < (info.getX() + card.getTexture().getWidth())) {
				if (info.getY() != 30) {
					info.setXY(info.getX(), 30);
					numberSelected++;
				}
				else {
					info.setXY(info.getX(), 0);
					numberSelected--;
				}
			}

		}

		return -1;
	}

	public class CardInfo {
		private float x;
		private float y;

		public CardInfo() {
			x = 0;
			y = 0;
		}

		public CardInfo(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public void setXY(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public float getX() {
			return this.x;
		}

		public float getY() {
			return this.y;
		}

	}

}
