package adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.core.util.TextAttr;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import newDB.CetRootWord;
import wordtest.WordDetailActiivty;

public class SimpleWordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {

    private int ITEM_TITLE = -1;
    private List<ViewHeader> headers = new ArrayList<>();

    Context context;
    boolean isTotal;
    List<CetRootWord> cetRootWords;
    List<Object> dataSource ;


    boolean showOrder;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private ArrayList<Object> list;

    public SimpleWordListAdapter(List<CetRootWord> cetRootWords, boolean isTotal) {
        this.cetRootWords = cetRootWords;
        this.isTotal = isTotal;
        dataSource = new ArrayList<>();
        dataSource.addAll(cetRootWords);
    }

    public void setShowOrder(boolean showOrder) {
        this.showOrder = showOrder;
        String s = "";
        for (int i = 0; i < dataSource.size(); i++) {
            if (!s.equals(((CetRootWord) dataSource.get(i)).word.substring(0, 1).toUpperCase())) {
                s = ((CetRootWord) dataSource.get(i)).word.substring(0, 1).toUpperCase();
                ViewHeader header = new ViewHeader(s, i);
                headers.add(header);
            }
        }

        int count = 0 ;
        for (ViewHeader header:headers) {
            header.setIndex(header.getIndex()+count);
            dataSource.add(header.getIndex(),header);
            count++;
        }
//
//        if (dataSource.get(1).equals("A")){
//            dataSource.remove(0);
//        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view;
        if (viewType == -1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_word_header, viewGroup, false);
            return new HeaderView(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordtest_simple_word_item, viewGroup, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {

        if (viewHolder instanceof HeaderView) {
            ((HeaderView) viewHolder).header.setText(((ViewHeader) dataSource.get(pos)).getHeader());
            ((HeaderView) viewHolder).Vheader = (ViewHeader) dataSource.get(pos);
        } else {
            CetRootWord rootWord = (CetRootWord) dataSource.get(pos);
            ((ViewHolder) viewHolder).setItem(rootWord, pos);
        }

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getItemCount();
        list = new ArrayList<>();
        if (count > 0) {
            list.add("↑");
            positionOfSection.put(0, 0);
            sectionOfPosition.put(0, 0);
            for (int i = 0; i < count; i++) {
                String letter = " ";
                if (dataSource.get(i) instanceof  CetRootWord){
                    letter = ((CetRootWord) dataSource.get(i)).word.substring(0,1).toUpperCase();

                }
                else if(dataSource.get(i) instanceof  HeaderView){
                    letter = ((ViewHeader) dataSource.get(i)).getHeader();

                }
                int section = list.size()-1 ;
                if (list.get(section) != null && !list.get(section).equals(letter)) {
                    list.add(letter);
                    section++;
                    positionOfSection.put(section, i);
                }
                sectionOfPosition.put(i, section);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return positionOfSection.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }


    public class HeaderView extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.header)
        TextView header;

        ViewHeader Vheader ;

        public HeaderView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (Vheader.getHeader()){

            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.word)
        TextView word;
        @BindView(R.id.pron)
        TextView pron;
        @BindView(R.id.def)
        TextView def;
        @BindView(R.id.info_img)
        ImageView info_img;

        CetRootWord cetRootWord;

        boolean isChecked = false;
        boolean shouldShow = true ;

        ObjectAnimator animator;
        ObjectAnimator animator_1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            info_img.setOnClickListener(this);
            animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 1f);
            animator_1 = ObjectAnimator.ofFloat(pron, "alpha", 0f, 1f);
            animator.setDuration(1200);
            animator_1.setDuration(1200);

        }

        public void setItem(CetRootWord rootWord, int position) {
            word.setText(rootWord.word);
            if (!rootWord.pron.startsWith("[")){
                pron.setText(String.format("[%s]", TextAttr.decode(rootWord.pron)));
            }else {
                pron.setText(String.format("%s", TextAttr.decode(rootWord.pron)));
            }
            def.setText(rootWord.def);
            setItemVisible();
            itemView.setTag(position);
            cetRootWord = rootWord;
            if (isTotal) {
                if (cetRootWord.wrong == 1) {
                    word.setTextColor(context.getResources().getColor(R.color.green));
                } else if (cetRootWord.wrong == 2) {
                    word.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.info_img:
                    WordDetailActiivty.start(context, cetRootWord);
                    break;
            }
            isChecked = !isChecked;
            animator.start();
            animator_1.start();
            setItemVisible();
        }

        private void setItemVisible() {
            if (isChecked) {
                def.setVisibility(View.VISIBLE);
                pron.setVisibility(View.GONE);
                word.setVisibility(View.GONE);
                info_img.setVisibility(View.VISIBLE);
            } else {
                def.setVisibility(View.GONE);
                pron.setVisibility(View.VISIBLE);
                word.setVisibility(View.VISIBLE);
                info_img.setVisibility(View.GONE);
            }
        }
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        for (ViewHeader header : headers) {
            if (header.getIndex() == position)
                return -1;
        }
        return position;
    }
}
