package pt.joaomneto.ffgbutil.adventure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import pt.joaomneto.ffgbutil.LoadAdventureActivity;
import pt.joaomneto.ffgbutil.R;
import pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureProvisionsFragment;
import pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureVitalStatsFragment;
import pt.joaomneto.ffgbutil.util.DiceRoller;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public abstract class Adventure extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	StandardSectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	Integer initialSkill = -1;
	Integer initialLuck = -1;
	Integer initialStamina = -1;
	Integer currentSkill = -1;
	Integer currentLuck = -1;
	Integer currentStamina = -1;
	List<String> equipment = new ArrayList<String>();
	List<String> notes = new ArrayList<String>();
	Integer currentReference = -1;

	// Common values
	Integer standardPotion = -1;
	Integer gold = -1;
	Integer provisions = -1;
	Integer provisionsValue = -1;
	Integer standardPotionValue = -1;

	File dir = null;
	int gamebook = -1;
	String name = null;
	Properties savedGame;

	protected static final int FRAGMENT_VITAL_STATS = 0;
	protected static final int FRAGMENT_COMBAT = 1;
	protected static final int FRAGMENT_PROVISIONS = 2;
	protected static final int FRAGMENT_EQUIPMENT = 3;
	protected static final int FRAGMENT_NOTES = 4;

	protected static SparseArray<Adventure.AdventureFragmentRunner> fragmentConfiguration = new SparseArray<Adventure.AdventureFragmentRunner>();

	public Adventure() {
		super();
		fragmentConfiguration
				.put(FRAGMENT_VITAL_STATS,
						new AdventureFragmentRunner(R.string.vitalStats,
								"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureVitalStatsFragment"));
		fragmentConfiguration
				.put(FRAGMENT_COMBAT,
						new AdventureFragmentRunner(R.string.fights,
								"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureCombatFragment"));
		fragmentConfiguration
				.put(FRAGMENT_PROVISIONS,
						new AdventureFragmentRunner(R.string.potionsProvisions,
								"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureProvisionsFragment"));
		fragmentConfiguration
				.put(FRAGMENT_EQUIPMENT,
						new AdventureFragmentRunner(R.string.goldEquipment,
								"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureEquipmentFragment"));
		fragmentConfiguration
				.put(FRAGMENT_NOTES,
						new AdventureFragmentRunner(R.string.notes,
								"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureNotesFragment"));
	}

	public static class AdventureFragmentRunner {
		int titleId;
		String className;

		public AdventureFragmentRunner(int titleId, String className) {
			super();
			this.titleId = titleId;
			this.className = className;
		}

		public int getTitleId() {
			return titleId;
		}

		public String getClassName() {
			return className;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
						
			// Create the adapter that will return a fragment for each of the
			// three
			// primary sections of the app.
			setContentView(R.layout.activity_adventure);
			mSectionsPagerAdapter = new StandardSectionsPagerAdapter(
					getSupportFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);

			String fileName = getIntent().getStringExtra(
					LoadAdventureActivity.ADVENTURE_FILE);
			String relDir = getIntent().getStringExtra(
					LoadAdventureActivity.ADVENTURE_DIR);
			name = relDir;
			dir = new File(Environment.getExternalStorageDirectory().getPath()
					+ "/ffgbutil/" + relDir);

			loadGameFromFile(dir, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadGameFromFile(File dir, String fileName)
			throws IOException, FileNotFoundException {
		savedGame = new Properties();
		savedGame.load(new FileInputStream(new File(dir, fileName)));

		gamebook = Integer.valueOf(savedGame.getProperty("gamebook"));
		initialSkill = Integer.valueOf(savedGame.getProperty("initialSkill"));
		initialLuck = Integer.valueOf(savedGame.getProperty("initialLuck"));
		initialStamina = Integer.valueOf(savedGame
				.getProperty("initialStamina"));
		currentSkill = Integer.valueOf(savedGame.getProperty("currentSkill"));
		currentLuck = Integer.valueOf(savedGame.getProperty("currentLuck"));
		currentStamina = Integer.valueOf(savedGame
				.getProperty("currentStamina"));

		String equipmentS = new String(savedGame.getProperty("equipment")
				.getBytes(java.nio.charset.Charset.forName("ISO-8859-1")));
		String notesS = new String(savedGame.getProperty("notes").getBytes(
				java.nio.charset.Charset.forName("ISO-8859-1")));
		currentReference = Integer.valueOf(savedGame
				.getProperty("currentReference"));

		if (equipmentS != null) {
			equipment = new ArrayList<String>();
			List<String> list = Arrays.asList(equipmentS.split("#"));
			for (String string : list) {
				if (!string.isEmpty())
					equipment.add(string);
			}
		}

		if (notesS != null) {
			notes = new ArrayList<String>();
			List<String> list = Arrays.asList(notesS.split("#"));
			for (String string : list) {
				if (!string.isEmpty())
					notes.add(string);
			}
		}

		loadAdventureSpecificValuesFromFile();
	}

	protected abstract void loadAdventureSpecificValuesFromFile();

	public void setCurrentSkill(Integer currentSkill) {
		AdventureVitalStatsFragment adventureVitalStatsFragment = getVitalStatsFragment();
		this.currentSkill = currentSkill;
		adventureVitalStatsFragment.updateValues();
	}

	public void setCurrentLuck(Integer currentLuck) {
		AdventureVitalStatsFragment adventureVitalStatsFragment = getVitalStatsFragment();
		this.currentLuck = currentLuck;
		adventureVitalStatsFragment.updateValues();
	}

	private AdventureVitalStatsFragment getVitalStatsFragment() {
		AdventureVitalStatsFragment adventureVitalStatsFragment = (AdventureVitalStatsFragment) getSupportFragmentManager()
				.getFragments().get(FRAGMENT_VITAL_STATS);
		return adventureVitalStatsFragment;
	}

	private AdventureProvisionsFragment getProvisionsFragment() {
		AdventureProvisionsFragment adventureProvisionsFragment = (AdventureProvisionsFragment) getSupportFragmentManager()
				.getFragments().get(FRAGMENT_PROVISIONS);
		return adventureProvisionsFragment;
	}

	public void setCurrentStamina(Integer currentStamina) {
		AdventureVitalStatsFragment adventureVitalStatsFragment = getVitalStatsFragment();
		this.currentStamina = currentStamina;
		adventureVitalStatsFragment.updateValues();
	}

	public void setCurrentReference(Integer currentReference) {
		this.currentReference = currentReference;
	}

	public void testSkill(View v) {

		boolean result = DiceRoller.roll2D6() < currentSkill;

		String message = result ? "Success!" : "Failed...";
		showAlert(message);
	}

	public void testLuck(View v) {

		boolean result = testLuckInternal();

		String message = result ? "Success!" : "Failed...";
		showAlert(message);
	}

	public boolean testLuckInternal() {
		boolean result = DiceRoller.roll2D6() < currentLuck;

		setCurrentLuck(--currentLuck);
		return result;
	}

	public void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Result")
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class StandardSectionsPagerAdapter extends FragmentPagerAdapter {

		public StandardSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			try {
				return (Fragment) Class.forName(
						fragmentConfiguration.get(position).getClassName())
						.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public int getCount() {
			return fragmentConfiguration.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();

			return getString(fragmentConfiguration.get(position).getTitleId())
					.toUpperCase(l);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adventure, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.rolld6:
			showAlert(DiceRoller.rollD6() + "");
			return true;
		case R.id.roll2d6:
			showAlert(DiceRoller.roll2D6() + "");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void savepoint(View v) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Current Reference?");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		final InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.requestFocus();
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				try {
					imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
					String ref = input.getText().toString();
					File file = new File(dir, ref + ".xml");

					BufferedWriter bw = new BufferedWriter(new FileWriter(file));

					storeValuesInFile(ref, bw);
					storeNotesForRestart(dir);

					bw.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
					}
				});

		alert.show();
	}

	private String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		return stringBuilder.toString();
	}

	private void storeNotesForRestart(File dir) throws IOException {

		String notesS = "";

		if (!notes.isEmpty()) {
			for (String note : notes) {
				notesS += note + "#";
			}
			notesS = notesS.substring(0, notesS.length() - 1);
		}
		
		String initialContent = readFile(new File(dir, "initial.xml"));
		initialContent = initialContent.replace("notes=", "notes="+notesS);
		
		FileWriter fileWriter = new FileWriter(new File(dir, "initial_full_notes.xml"));
		BufferedWriter bw = new BufferedWriter(fileWriter);
		bw.write(initialContent);
		
		bw.close();
		fileWriter.close();

	}

	public void storeValuesInFile(String ref, BufferedWriter bw)
			throws IOException {
		String equipmentS = "";
		String notesS = "";

		if (!notes.isEmpty()) {
			for (String note : notes) {
				notesS += note + "#";
			}
			notesS = notesS.substring(0, notesS.length() - 1);
		}

		if (!equipment.isEmpty()) {
			for (String eq : equipment) {
				equipmentS += eq + "#";
			}
			equipmentS = equipmentS.substring(0, equipmentS.length() - 1);
		}
		bw.write("gamebook=" + gamebook + "\n");
		bw.write("name=" + name + "\n");
		bw.write("initialSkill=" + initialSkill + "\n");
		bw.write("initialLuck=" + initialSkill + "\n");
		bw.write("initialStamina=" + initialStamina + "\n");
		bw.write("currentSkill=" + currentSkill + "\n");
		bw.write("currentLuck=" + currentLuck + "\n");
		bw.write("currentStamina=" + currentStamina + "\n");
		bw.write("currentReference=" + ref + "\n");
		bw.write("equipment=" + equipmentS + "\n");
		bw.write("notes=" + notesS + "\n");
		storeAdventureSpecificValuesInFile(bw);
	}

	public abstract void storeAdventureSpecificValuesInFile(BufferedWriter bw)
			throws IOException;

	public void consumePotion(View view) {
		if (standardPotionValue == 0) {
			showAlert("You have no potion left...");
		} else {
			AdventureProvisionsFragment adventureProvisionsFragment = getProvisionsFragment();
			adventureProvisionsFragment.setPotionValue(--standardPotionValue);
			String message = "";
			switch (standardPotion) {
			case 0:
				message = "You have replenished your Skill level!";
				setCurrentSkill(getInitialSkill());
				break;
			case 1:
				message = "You have replenished your Stamina level!";
				setCurrentStamina(getInitialStamina());
				break;
			case 2:
				message = "You have replenished your Luck level (+1)!";
				setCurrentLuck(getInitialLuck() + 1);
				break;
			}
			showAlert(message);
		}
	}

	public void consumeProvision(View view) {
		if (provisions == 0) {
			showAlert("You have no provisions left...");
		} else if (getCurrentStamina() == getInitialStamina()) {
			showAlert("You are already at maximum Stamina!");
		} else {
			AdventureProvisionsFragment adventureProvisionsFragment = getProvisionsFragment();
			adventureProvisionsFragment.setProvisionsValue(--provisions);
			setCurrentStamina(getCurrentStamina() + provisionsValue);
			if (getCurrentStamina() > getInitialStamina())
				setCurrentStamina(getInitialStamina());
			showAlert("You have gained 4 Stamina points!");
		}
	}

	public StandardSectionsPagerAdapter getmSectionsPagerAdapter() {
		return mSectionsPagerAdapter;
	}

	public void setmSectionsPagerAdapter(
			StandardSectionsPagerAdapter mSectionsPagerAdapter) {
		this.mSectionsPagerAdapter = mSectionsPagerAdapter;
	}

	public ViewPager getmViewPager() {
		return mViewPager;
	}

	public void setmViewPager(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	public Integer getInitialSkill() {
		return initialSkill;
	}

	public void setInitialSkill(Integer initialSkill) {
		this.initialSkill = initialSkill;
	}

	public Integer getInitialLuck() {
		return initialLuck;
	}

	public void setInitialLuck(Integer initialLuck) {
		this.initialLuck = initialLuck;
	}

	public Integer getInitialStamina() {
		return initialStamina;
	}

	public void setInitialStamina(Integer initialStamina) {
		this.initialStamina = initialStamina;
	}

	public List<String> getEquipment() {
		return equipment;
	}

	public void setEquipment(List<String> equipment) {
		this.equipment = equipment;
	}

	public List<String> getNotes() {
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}

	public Integer getStandardPotion() {
		return standardPotion;
	}

	public void setStandardPotion(Integer standardPotion) {
		this.standardPotion = standardPotion;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getProvisions() {
		return provisions;
	}

	public void setProvisions(Integer provisions) {
		this.provisions = provisions;
	}

	public Integer getStandardPotionValue() {
		return standardPotionValue;
	}

	public void setStandardPotionValue(Integer standardPotionValue) {
		this.standardPotionValue = standardPotionValue;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public int getGamebook() {
		return gamebook;
	}

	public void setGamebook(int gamebook) {
		this.gamebook = gamebook;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Properties getSavedGame() {
		return savedGame;
	}

	public void setSavedGame(Properties savedGame) {
		this.savedGame = savedGame;
	}

	public Integer getCurrentSkill() {
		return currentSkill;
	}

	public Integer getCurrentLuck() {
		return currentLuck;
	}

	public Integer getCurrentStamina() {
		return currentStamina;
	}

	public Integer getCurrentReference() {
		return currentReference;
	}

	public synchronized Integer getProvisionsValue() {
		return provisionsValue;
	}

	public synchronized void setProvisionsValue(Integer provisionsValue) {
		this.provisionsValue = provisionsValue;
	}

	@Override
	protected void onPause() {
		super.onPause();

		pause();
	}

	private void pause() {
		try {
			File file = new File(dir, "temp" + ".xml");

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			storeValuesInFile("-1", bw);

			bw.close();
		} catch (IOException e) {
			try {
				FileWriter fw = new FileWriter(new File(dir, "exception_"
						+ new Date() + ".txt"), true);
				PrintWriter pw = new PrintWriter(fw);
				e.printStackTrace(pw);
				pw.close();
				fw.close();
			} catch (IOException e1) {
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		resume();
	}

	private void resume() {
		try {
			
			File f = new File(dir, "temp.xml");
			
			if(!f.exists())
				return;
			
			loadGameFromFile(dir, "temp.xml");
			
			refreshScreens();
			
		} catch (Exception e) {
			try {
				FileWriter fw = new FileWriter(new File(dir, "exception_"
						+ new Date() + ".txt"), true);
				PrintWriter pw = new PrintWriter(fw);
				e.printStackTrace(pw);
				pw.close();
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void testResume(View v){
		resume();
	}
	
	public void testPause(View v){
		pause();
	}
	
	public void refreshScreens(){
		
		List<Fragment> afList = getSupportFragmentManager().getFragments();
		
		for (Fragment fragment : afList) {
			AdventureFragment af = (AdventureFragment) fragment;
			af.refreshScreensFromResume();
		}
	}

}
