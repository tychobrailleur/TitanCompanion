package pt.joaomneto.ffgbutil.adventure.impl.fragments.ff;

import pt.joaomneto.ffgbutil.R;
import pt.joaomneto.ffgbutil.adventure.AdventureFragment;
import pt.joaomneto.ffgbutil.adventure.impl.FFAdventure;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class FFVehicleStatsFragment extends DialogFragment implements AdventureFragment {

	TextView rocketsValue = null;
	TextView ironSpikesValue = null;
	TextView oilValue = null;
	TextView spareWheelsValue = null;

	Button minusRocketsButton = null;
	Button minusIronSpikesButton = null;
	Button plusRocketsButton = null;
	Button plusIronSpikesButton = null;

	Button minusOilButton = null;
	Button minusSpareWheelsButton = null;
	Button plusOilButton = null;
	Button plusSpareWheelsButton = null;
	
	Button addEnhancement = null;
	ListView enhancementList = null;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_13ff_adventure_vehicleequipment, container, false);

		final FFAdventure adv = (FFAdventure) getActivity();

		rocketsValue = (TextView) rootView.findViewById(R.id.statsRocketsValue);
		ironSpikesValue = (TextView) rootView.findViewById(R.id.statsIronSpikesValue);
		oilValue = (TextView) rootView.findViewById(R.id.statsOilValue);
		spareWheelsValue = (TextView) rootView.findViewById(R.id.spareWheelsValue);

		rocketsValue.setText(""+adv.getRockets());
		ironSpikesValue.setText(""+adv.getIronSpikes());
		oilValue.setText(""+adv.getOilCannisters());
		oilValue.setText(""+adv.getSpareWheels());

		minusRocketsButton = (Button) rootView.findViewById(R.id.minusRocketsButton);
		minusIronSpikesButton = (Button) rootView.findViewById(R.id.minusIronSpikesButton);
		plusRocketsButton = (Button) rootView.findViewById(R.id.plusRocketsButton);
		plusIronSpikesButton = (Button) rootView.findViewById(R.id.plusIronSpikesButton);

		minusOilButton = (Button) rootView.findViewById(R.id.minusOilButton);
		minusSpareWheelsButton = (Button) rootView.findViewById(R.id.minusSpareWheelsButton);
		plusOilButton = (Button) rootView.findViewById(R.id.plusOilButton);
		plusSpareWheelsButton = (Button) rootView.findViewById(R.id.plusSpareWheelsButton);
		
		enhancementList = (ListView) rootView.findViewById(R.id.carEnhancementList);
		addEnhancement = (Button) rootView.findViewById(R.id.buttonAddEnhancement);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(adv,
	            android.R.layout.simple_list_item_1, android.R.id.text1, adv.getCarEnhancements());
		enhancementList.setAdapter(adapter);
		
		addEnhancement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(adv);

				alert.setTitle("Note");

				// Set an EditText view to get user input
				final EditText input = new EditText(adv);
				InputMethodManager imm = (InputMethodManager) adv.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
				input.requestFocus();
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							@SuppressWarnings("unchecked")
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String value = input.getText().toString();
								adv.getCarEnhancements().add(value);
								((ArrayAdapter<String>)enhancementList.getAdapter()).notifyDataSetChanged();
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();
			}

		});
		
		enhancementList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int position = arg2;
				AlertDialog.Builder builder = new AlertDialog.Builder(adv);
				builder.setTitle("Delete note?")
						.setCancelable(false)
						.setNegativeButton("Close",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
						builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@SuppressWarnings("unchecked")
							public void onClick(DialogInterface dialog, int which) {
								adv.getCarEnhancements().remove(position);
								((ArrayAdapter<String>)enhancementList.getAdapter()).notifyDataSetChanged();
							}
						});
						
				AlertDialog alert = builder.create();
				alert.show();
				return true;
				
			}
		});

		minusRocketsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setRockets(Math.max(adv.getRockets() - 1, 0));
				refreshScreensFromResume();
			}
		});

		minusIronSpikesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setIronSpikes(Math.max(adv.getIronSpikes() - 1, 0));
				refreshScreensFromResume();

			}
		});

		plusRocketsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setRockets(adv.getRockets() + 1);
				refreshScreensFromResume();

			}
		});

		plusIronSpikesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setIronSpikes(adv.getIronSpikes() + 1);
				refreshScreensFromResume();

			}
		});

		minusOilButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setOilCannisters(Math.max(0, adv.getOilCannisters()-1));
				refreshScreensFromResume();

			}
		});

		minusSpareWheelsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setSpareWheels(Math.max(0, adv.getSpareWheels()-1));
				refreshScreensFromResume();

			}
		});
		plusOilButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setOilCannisters(adv.getOilCannisters()+1);
				refreshScreensFromResume();

			}
		});
		plusSpareWheelsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adv.setSpareWheels(adv.getSpareWheels()+1);
				refreshScreensFromResume();

			}
		});

	
		refreshScreensFromResume();

		return rootView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refreshScreensFromResume() {
		
		FFAdventure adv = (FFAdventure) getActivity();
		
		spareWheelsValue.setText("" + adv.getSpareWheels());
		oilValue.setText("" + adv.getOilCannisters());
		ironSpikesValue.setText("" + adv.getIronSpikes());
		rocketsValue.setText("" + adv.getRockets());
		((ArrayAdapter<String>) enhancementList.getAdapter()).notifyDataSetChanged();
	}
}