package xcong.diarygram10;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 28907 on 2016/9/23.
 */
public class CustomFontTextView extends TextView{
    public CustomFontTextView(Context context){
        super(context);
        init(context);
    }
    public CustomFontTextView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }
    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        init(context);
    }
    private void init(Context context){
        AssetManager assertMgr = context.getAssets();
        Typeface font = Typeface.createFromAsset(assertMgr, "fonts/OPTIAgency-Gothic.otf");
        setTypeface(font);
    }
}
