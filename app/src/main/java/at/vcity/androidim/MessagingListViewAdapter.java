package at.vcity.androidim;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.vcity.androidim.model.UserMessage;

/**
 * Created by akbank on 27/11/15.
 */
public class MessagingListViewAdapter extends BaseAdapter {
    private  Context context;
    private ArrayList<UserMessage> userMessagesList;
    private static LayoutInflater inflater=null;
    UserMessage userMessage;

    public MessagingListViewAdapter(Context context, ArrayList userMessagesList) {
        this.context = context;
        this.userMessagesList = userMessagesList;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userMessagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }else {
            holder=(ViewHolder)vi.getTag();
        }
        if(userMessagesList.size() < 0){
            holder.text.setText("No Data");
        }else {
            userMessage=null;
            userMessage = ( UserMessage ) userMessagesList.get( position );
            holder.text.setText( userMessage.getUsername() + " : " + userMessage.getMessage() );
        }
        return vi;
    }

    public static class ViewHolder{

        public TextView text;


    }

}
