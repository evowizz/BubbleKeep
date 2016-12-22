package cf.VoxStudio.bubblekeep.Services;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;


@TargetApi(Build.VERSION_CODES.N) // Only created on N+
public class BubbleKeepTileService extends TileService {

    public final static String Keep = "com.google.android.keep";
    public final static String Activity = Keep + ".activities.ShareReceiverActivity";


    @Override
    public void onClick() {
        Intent BKTS = new Intent();
        BKTS.setClassName(Keep, Activity);
        BKTS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(BKTS);
    }

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
    }

}