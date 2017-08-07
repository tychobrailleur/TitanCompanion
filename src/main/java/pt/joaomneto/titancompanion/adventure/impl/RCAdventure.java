package pt.joaomneto.titancompanion.adventure.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.joaomneto.titancompanion.R;
import pt.joaomneto.titancompanion.adventure.impl.fragments.rc.Robot;
import pt.joaomneto.titancompanion.adventure.impl.fragments.rc.RobotSpecialAbility;
import pt.joaomneto.titancompanion.adventure.impl.fragments.rc.RobotSpeed;

public class RCAdventure extends TFODAdventure {

	protected Robot currentRobot = null;
	protected List<Robot> robots = new ArrayList<Robot>();

	public List<Robot> getRobots() {
		return robots;
	}

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
	}


	public static final int FRAGMENT_ROBOTS = 1;
	public static final int FRAGMENT_ROBOTCOMBAT = 2;

	public RCAdventure() {
		super();
		int fragIndex = 0;
		fragmentConfiguration.clear();
		fragmentConfiguration.put(fragIndex++, new AdventureFragmentRunner(R.string.vitalStats,
				"pt.joaomneto.titancompanion.adventure.impl.fragments.rc.RCAdventureVitalStatsFragment"));
		fragmentConfiguration.put(fragIndex++, new AdventureFragmentRunner(R.string.robots,
				"pt.joaomneto.titancompanion.adventure.impl.fragments.rc.RCAdventureRobotFragment"));
		fragmentConfiguration.put(fragIndex++, new AdventureFragmentRunner(R.string.robotFights,
				"pt.joaomneto.titancompanion.adventure.impl.fragments.rc.RCAdventureRobotCombatFragment"));
		fragmentConfiguration.put(fragIndex++, new AdventureFragmentRunner(R.string.fights,
				"pt.joaomneto.titancompanion.adventure.impl.fragments.AdventureCombatFragment"));
		fragmentConfiguration.put(fragIndex++, new AdventureFragmentRunner(R.string.goldEquipment,
				"pt.joaomneto.titancompanion.adventure.impl.fragments.AdventureEquipmentFragment"));
		fragmentConfiguration.put(fragIndex++, new AdventureFragmentRunner(R.string.notes,
				"pt.joaomneto.titancompanion.adventure.impl.fragments.AdventureNotesFragment"));
	}

	public Robot getCurrentRobot() {
		return currentRobot;
	}

	public void setCurrentRobot(Robot currentRobot) {
		this.currentRobot = currentRobot;
	}

	@Override
	public void storeAdventureSpecificValuesInFile(BufferedWriter bw) throws IOException {
		super.storeAdventureSpecificValuesInFile(bw);

		String robotsS = "";

		for (Robot r : robots) {
			String name = r.getName();
			String location = r.getLocation() != null ? r.getLocation() : "";
			RobotSpeed speed = r.getSpeed();
			Integer armor = r.getArmor();
			String ability = r.getRobotSpecialAbility() != null ? r.getRobotSpecialAbility().getReference() + "" : "";
			String alternateName = r.getAlternateForm() != null ? r.getAlternateForm().getName() : "";
			robotsS += name + "§" + location + "§" + speed.name() + "§" + armor + "§" + ability + "§" + r.getBonus() + "§" + alternateName + "§" + r.isActive()
					+ "#";
		}
		if (robots.size()>0) {
			robotsS = robotsS.substring(0, robotsS.length() - 1);
		}

		bw.write("standardPotion=" + getStandardPotion() + "\n");
		bw.write("robots=" + robotsS + "\n");
	}

	@Override
	protected void loadAdventureSpecificValuesFromFile() {
		super.loadAdventureSpecificValuesFromFile();

		String robotsS = new String(getSavedGame().getProperty("robots").getBytes(java.nio.charset.Charset.forName("UTF-8")));

		Map<String, Robot> robotCache = new HashMap<String, Robot>();
		Map<Robot, String> robotAltCache = new HashMap<Robot, String>();

		if (robotsS != null) {
			robots = new ArrayList<Robot>();
			List<String> list = Arrays.asList(robotsS.split("#", -1));
			for (String string : list) {
				if (!string.isEmpty()) {
					String[] split = string.split("§", -1);
					String specialAbility = split[4];
					String alternateFormName = split[6];
					boolean active = Boolean.parseBoolean(split[7]);
					Robot robot = new Robot(split[0], Integer.parseInt(split[3]), RobotSpeed.valueOf(split[2]), Integer.parseInt(split[5]),
							RobotSpecialAbility.getAbiliyByReference(specialAbility.isEmpty() ? null : Integer.parseInt(specialAbility)));
					robot.setLocation(split[1]);
					robot.setActive(active);
					currentRobot = robot;
					robotCache.put(robot.getName(), robot);
					if (!alternateFormName.isEmpty())
						robotAltCache.put(robot, alternateFormName);
					robots.add(robot);
				}
			}
		}

		for (Robot r : robots) {
			if (robotAltCache.containsKey(r)) {
				r.setAlternateForm(robotCache.get(robotAltCache.get(r)));
			}
		}

	}

	public void destroyCurrentRobot() {
		robots.remove(currentRobot);
		robots.remove(currentRobot.getAlternateForm());
		currentRobot = null;
		fullRefresh();
		
	}

}