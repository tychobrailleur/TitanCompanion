package pt.joaomneto.ffgbutil.adventure.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.joaomneto.ffgbutil.R;
import pt.joaomneto.ffgbutil.adventure.Adventure;
import android.os.Bundle;
import android.view.Menu;

public class FFAdventure extends Adventure {
	
	
	private Integer currentFirepower = -1;
	private Integer currentArmour = -1;
	private Integer initialFirepower = -1;
	private Integer initialArmour = -1;
	private Integer rockets = -1;
	private Integer ironSpikes = -1;
	private Integer oilCannisters = -1;
	private Integer spareWheels = -1;
	
	List<String> carEnhancements = new ArrayList<String>();

	protected static final int FRAGMENT_VEHICLE_COMBAT = 2;
	protected static final int FRAGMENT_VEHICLE_EQUIPMENT = 3;
	protected static final int FRAGMENT_EQUIPMENT = 4;
	protected static final int FRAGMENT_NOTES = 5;

	public FFAdventure() {
		super();
		fragmentConfiguration.clear();
		fragmentConfiguration.put(FRAGMENT_VITAL_STATS, new AdventureFragmentRunner(R.string.vitalStats,
				"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureVitalStatsFragment"));
		fragmentConfiguration.put(FRAGMENT_COMBAT, new AdventureFragmentRunner(R.string.fights,
				"pt.joaomneto.ffgbutil.adventure.impl.fragments.ff.FFAdventureCombatFragment"));
		fragmentConfiguration.put(FRAGMENT_VEHICLE_COMBAT, new AdventureFragmentRunner(R.string.vehicleCombat,
				"pt.joaomneto.ffgbutil.adventure.impl.fragments.ff.FFVehicleCombatFragment"));
		fragmentConfiguration.put(FRAGMENT_VEHICLE_EQUIPMENT, new AdventureFragmentRunner(R.string.vehicleCombat,
				"pt.joaomneto.ffgbutil.adventure.impl.fragments.ff.FFVehicleStatsFragment"));
		fragmentConfiguration.put(FRAGMENT_EQUIPMENT, new AdventureFragmentRunner(R.string.goldEquipment,
				"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureEquipmentFragment"));
		fragmentConfiguration.put(FRAGMENT_NOTES, new AdventureFragmentRunner(R.string.notes,
				"pt.joaomneto.ffgbutil.adventure.impl.fragments.AdventureNotesFragment"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.adventure, menu);
		return true;
	}

	@Override
	public void storeAdventureSpecificValuesInFile(BufferedWriter bw) throws IOException {


		bw.write("currentFirepower="+currentFirepower+"\n");
		bw.write("currentArmour="+currentArmour+"\n");
		bw.write("initialFirepower="+initialFirepower+"\n");
		bw.write("initialArmour="+initialArmour+"\n");
		bw.write("rockets="+rockets+"\n");
		bw.write("ironSpikes="+ironSpikes+"\n");
		bw.write("oilCannisters="+oilCannisters+"\n");
		bw.write("spareWheels="+spareWheels+"\n");
		
		bw.write("gold=" + getGold() + "\n");
		bw.write("provisions=" + getProvisions() + "\n");
		bw.write("provisionsValue=" + getProvisionsValue() + "\n");
		bw.write("carEnhancements="+arrayToString(carEnhancements));
	}

	


	public Integer getCurrentFirepower() {
		return currentFirepower;
	}

	public void setCurrentFirepower(Integer currentFirepower) {
		this.currentFirepower = currentFirepower;
	}

	public Integer getCurrentArmour() {
		return currentArmour;
	}

	public void setCurrentArmour(Integer currentArmour) {
		this.currentArmour = currentArmour;
	}

	public Integer getInitialFirepower() {
		return initialFirepower;
	}

	public void setInitialFirepower(Integer initialFirepower) {
		this.initialFirepower = initialFirepower;
	}

	public Integer getInitialArmour() {
		return initialArmour;
	}

	public void setInitialArmour(Integer initialArmour) {
		this.initialArmour = initialArmour;
	}

	public Integer getRockets() {
		return rockets;
	}

	public void setRockets(Integer rockets) {
		this.rockets = rockets;
	}

	public Integer getIronSpikes() {
		return ironSpikes;
	}

	public void setIronSpikes(Integer ironSpikes) {
		this.ironSpikes = ironSpikes;
	}

	public Integer getOilCannisters() {
		return oilCannisters;
	}

	public void setOilCannisters(Integer oilCannisters) {
		this.oilCannisters = oilCannisters;
	}

	@Override
	protected void loadAdventureSpecificValuesFromFile() {

		setCurrentFirepower(Integer.valueOf(getSavedGame().getProperty("currentFirepower")));
		setCurrentArmour(Integer.valueOf(getSavedGame().getProperty("currentArmour")));
		setInitialFirepower(Integer.valueOf(getSavedGame().getProperty("initialFirepower")));
		setInitialArmour(Integer.valueOf(getSavedGame().getProperty("initialArmour")));
		setRockets(Integer.valueOf(getSavedGame().getProperty("rockets")));
		setIronSpikes(Integer.valueOf(getSavedGame().getProperty("ironSpikes")));
		setOilCannisters(Integer.valueOf(getSavedGame().getProperty("oilCannisters")));
		setGold(Integer.valueOf(getSavedGame().getProperty("gold")));
		setProvisions(Integer.valueOf(getSavedGame().getProperty("provisions")));
		setProvisionsValue(Integer.valueOf(getSavedGame().getProperty("provisionsValue")));
		setSpareWheels(Integer.valueOf(getSavedGame().getProperty("spareWheels")));
		setCarEnhancements(stringToArray(getSavedGame().getProperty("provisionsValue")));
	}

	public Integer getSpareWheels() {
		return spareWheels;
	}

	public void setSpareWheels(Integer spareWheels) {
		this.spareWheels = spareWheels;
	}

	public List<String> getCarEnhancements() {
		return carEnhancements;
	}

	public void setCarEnhancements(List<String> carEnhancements) {
		this.carEnhancements = carEnhancements;
	}
	
	
	
	

	
}