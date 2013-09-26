package pt.joaomneto.ffgbutil.adventurecreation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Random;

import pt.joaomneto.ffgbutil.GamebookSelectionActivity;
import pt.joaomneto.ffgbutil.R;
import pt.joaomneto.ffgbutil.adventurecreation.fragments.PotionsFragment;
import pt.joaomneto.ffgbutil.adventurecreation.fragments.VitalStatisticsFragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public abstract class AdventureCreation extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	protected SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;
	

	protected static Random random = new Random(System.currentTimeMillis()); 
	protected int skill = -1;
	protected int luck = -1;
	protected int stamina = -1;
	protected int potion = -1;
	protected int gamebook = -1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adventure_creation, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case 0:
				fragment = new VitalStatisticsFragment();
				break;
			case 1:
				fragment = new PotionsFragment();
				break;
			default:
				fragment = null;
				break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_adventure_creation_vitalstats).toUpperCase(l);
			case 1:
				return getString(R.string.title_adventure_creation_potions).toUpperCase(l);
			}
			return null;
		}
	}
	

	
	public void rollStats(View view){
		skill = random.nextInt(5)+7;
		luck = random.nextInt(5)+7;
		stamina = random.nextInt(11)+13;
				
		TextView skillValue = (TextView) findViewById(R.id.skillValue);
		TextView staminaValue = (TextView) findViewById(R.id.staminaValue);
		TextView luckValue = (TextView) findViewById(R.id.luckValue);
		
		skillValue.setText(""+skill);
		staminaValue.setText(""+stamina);
		luckValue.setText(""+luck);
	}
	
	
	
	public void saveAdventure(View view){
		try {
			EditText et = (EditText) findViewById(R.id.adventureNameInput);
			
			File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/ffgbutil/");
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			File file = new File(dir, "save_"+et.getText().toString().replace(' ', '-')+".xml");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			bw.write("gamebook="+gamebook+"\n");
			bw.write("name="+et.getText().toString()+"\n");
			bw.write("initial_skill="+skill+"\n");
			bw.write("initial_luck="+luck+"\n");
			bw.write("initial_stamina="+stamina+"\n");
			bw.write("current_skill="+skill+"\n");
			bw.write("current_luck="+luck+"\n");
			bw.write("current_stamina="+stamina+"\n");
			bw.write("standard_potion="+potion+"\n");
			
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized int getPotion() {
		return potion;
	}

	public synchronized void setPotion(int potion) {
		this.potion = potion;
	}
	

}
