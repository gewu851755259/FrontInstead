###问题背景###
之前在编写[鲤鱼日语](https://github.com/54wall/MyJapanese)时，因为使用了外部的字体，这导致了一个问题就是我的ListView显示的是圆滑的字体，但是其他dialog、Spinner就全都是Android默认字体，这样不统一就会很难看。根据拿来主义，我找到了一个利用反射更换全局字体的方法，而且不需要使用外部库，够我用了。
代码已经上传GitHub（字体自己下载吧，太大了）-[54wall/FrontInstead](https://github.com/54wall/FrontInstead)
####新建继承Application类的SetAppTypeface
因为是全局更换字体，所以需要使用Application来完成全局的作用：
```
android.app.Application
Base class for those who need to maintain global application state. You can provide your own implementation by specifying its name in your AndroidManifest.xml's <application> tag, which will cause that class to be instantiated for you when the process for your application/package is created. 
```
说明比较易懂，就是他可以控制一个app全局的状态，还提示可以在
AndroidManifest.xml's <application> tag这个标签中进行设置。
以下是SetAppTypeface.java全部代码
```
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
        //华文彩云，加载外部字体assets/front/huawen_caiyun.ttf
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/huawen_caiyun.ttf");
        try
        {    
            //与values/styles.xml中的<item name="android:typeface">sans</item>对应
//            Field field = Typeface.class.getDeclaredField("SERIF");
//            field.setAccessible(true);
//            field.set(null, typeFace);
            
//            Field field_1 = Typeface.class.getDeclaredField("DEFAULT");
//            field_1.setAccessible(true);
//            field_1.set(null, typeFace);
            
            //与monospace对应
//            Field field_2 = Typeface.class.getDeclaredField("MONOSPACE");
//            field_2.setAccessible(true);
//            field_2.set(null, typeFace);
            
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

```

Field这个类就是反射了，他可以获取相应字段，然后就可以通过反射设置不能设置的属性了（具体其实我也不是很懂）。

#####为什么是SERIF-MONOSPACE-SANS_SERIF这几个字符串
直接进入Typeface这个类，你会发现：


```
    static {
        init();
        // Set up defaults and typefaces exposed in public API
        DEFAULT         = create((String) null, 0);
        DEFAULT_BOLD    = create((String) null, Typeface.BOLD);
        SANS_SERIF      = create("sans-serif", 0);
        SERIF           = create("serif", 0);
        MONOSPACE       = create("monospace", 0);

        sDefaults = new Typeface[] {
            DEFAULT,
            DEFAULT_BOLD,
            create((String) null, Typeface.ITALIC),
            create((String) null, Typeface.BOLD_ITALIC),
        };

    }
```

create就是在加载字体了。而SetAppTypeface这个类继承自Application，Android App启动后会首先加载这个类，实现反射替换App中的全部使用SANS_SERIF的字体，这样配合values/styles.xml进行全局默认字体样式选择就可以进行全局字体的更换了。

####修改values/styles.xml
在styles.xml文件中找到<style name="AppTheme" parent="AppBaseTheme">并修改成下边的

```
    <!-- Application theme. -->
    <style name="AppTheme" parent="AppBaseTheme">
        <!-- All customizations that are NOT specific to a particular API-level can go here. -->
        <item name="android:typeface">sans</item>
    </style>
```

这里android:typeface可以设置的仅仅有normal、sans、serif、monospace可以设置，因为我在SetAppTypeface类中设置的是Typeface.class.getDeclaredField("SANS_SERIF");
所以我这里便设置成sans，如果getDeclaredField()设置的是其他的类型，则要选择同类型的其他诸如serif、monospace等等
####修改AndroidManifest.xml
进入AndroidManifest.xml找到application这个tag，直接在其内部增加android:name=".SetAppTypeface"，完成后如下：
<application
        android:allowBackup="true"
        **android:name=".SetAppTypeface"**
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
上边三步完成后就可以启动App看看效果了：
我的MainActivity如下：

```
package pri.weiqiang.frontinstead;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
/**
 * @author 54wall
 * @date 创建时间：2016-7-28 下午2:20:59
 * @version 1.0
 */
public class MainActivity extends Activity {
    public TextView textView01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView01=(TextView)super.findViewById(R.id.TextView01);
        textView01.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

```
简单说一下，显示内容其实在布局文件activity_main中，这里设置其中一个TextView字体风格为Typeface.NORMAL，这样的话，因为他不在默认使用styles.xml中的sans字体，所以全局对他来说便没有作用了。
效果如下：
![快速使用反射更换Android全局字体.jpg](https://github.com/54wall/FrontInstead/blob/master/快速使用反射更换Android全局字体.jpg)
####需要注意

[Android如何高效率的替换整个APP的字体?](https://www.zhihu.com/question/38615247)

其余自行百度或谷歌。
