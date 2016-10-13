package pri.weiqiang.frontinstead;

import java.lang.reflect.Field;
import android.app.Application;
import android.graphics.Typeface;
/**
 * @author 54wall
 * @date 创建时间：2016-7-28 下午2:20:59
 * @version 1.0
 */
public class SetAppTypeface extends Application{
	public static Typeface typeFace;

	@Override
	public void onCreate() {
		super.onCreate();
		setTypeface();
	}
	public void setTypeface(){
		//华文彩云
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/huawen_caiyun.ttf");
		try
		{	
			//与values/styles.xml中的<item name="android:typeface">sans</item>对应
//			Field field = Typeface.class.getDeclaredField("SERIF");
//			field.setAccessible(true);
//			field.set(null, typeFace);
			
//			Field field_1 = Typeface.class.getDeclaredField("DEFAULT");
//			field_1.setAccessible(true);
//			field_1.set(null, typeFace);
			
			//与monospace对应
//			Field field_2 = Typeface.class.getDeclaredField("MONOSPACE");
//			field_2.setAccessible(true);
//			field_2.set(null, typeFace);
			
			//与values/styles.xml中的<item name="android:typeface">sans</item>对应
			Field field_3 = Typeface.class.getDeclaredField("SANS_SERIF");
			field_3.setAccessible(true);
			field_3.set(null, typeFace);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}	
	}
}
