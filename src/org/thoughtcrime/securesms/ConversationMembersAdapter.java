package org.thoughtcrime.securesms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class ConversationMembersAdapter extends RecyclerView.Adapter<ConversationMembersAdapter.ViewHolder> {

    private Context context;
    private List<GroupMembersList> groupMembersList;

   public ConversationMembersAdapter(Context context, List groupmemberslist){
       this.context=context;

   }

    @Override
    public ConversationMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_members_list_row, parent, false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationMembersAdapter.ViewHolder holder, int position) {

    GroupMembersList item = groupMembersList.get(position);

    holder.name.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return groupMembersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.participant_name);
        }

    }
}
