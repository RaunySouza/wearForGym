package br.com.rauny.wearforgym;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import br.com.rauny.wearforgym.annotation.Layout;
import br.com.rauny.wearforgym.core.model.Time;
import br.com.rauny.wearforgym.recyclerView.DividerItemDecoration;
import br.com.rauny.wearforgym.recyclerView.RecyclerItemClickListener;
import br.com.rauny.wearforgym.timerRecyclerView.TimeRecyclerViewAdapter;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RecyclerView timeRecyclerView = (RecyclerView) findViewById(R.id.time_recycler_view);
		timeRecyclerView.setHasFixedSize(true);
		timeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		timeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		timeRecyclerView.setAdapter(new TimeRecyclerViewAdapter(getTimeList()));
		timeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
						new RecyclerItemClickListener.OnItemClickListener() {
							@Override
							public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, final int position) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
										for (Node node : nodes.getNodes()) {
											PutDataMapRequest request = PutDataMapRequest.create("/time");
											request.getDataMap().putInt("selected.time", position);
											PutDataRequest putDataRequest = request.asPutDataRequest();
											DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest).await();
											if (result.getStatus().isSuccess()) {
												Log.i(TAG, "Data Sent with Success to node: " + node.getDisplayName());
											}
											else {
												Log.i(TAG, "Couldn't send data");
											}
										}
									}
								}).start();
							}
						})
		);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();
	}

	@Override
	protected int navigationDrawerItem() {
		return R.id.item_menu_home;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Wearable.DataApi.removeListener(mGoogleApiClient, this);
		mGoogleApiClient.disconnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private List<Time> getTimeList() {
		List<Time> times = new ArrayList<>();
		times.add(new Time(10000, "10 Segundos"));
		times.add(new Time(20000, "20 Segundos"));
		times.add(new Time(30000, "30 Segundos"));
		times.add(new Time(40000, "40 Segundos"));
		times.add(new Time(50000, "50 Segundos"));
		times.add(new Time(60000, "1 Minuto"));
		return times;
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.i(TAG, "Connected to device");
		Wearable.DataApi.addListener(mGoogleApiClient, this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "Suspend Connection with device");
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEventBuffer) {
		Log.i(TAG, "Data was changed!");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "Cannot be able to connect to device");
	}
}
