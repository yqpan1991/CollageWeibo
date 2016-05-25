package apollo.edus.collageweibo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.AbsListView;


import java.lang.reflect.Field;

import apollo.edus.collageweibo.R;

public class ListViewUtil {

	/**
	 * We don't want to see the overscroll effect on the home page, and the
	 * method provided by Google has side effect, which will bounce back, so
	 * just replace the edge and glow drawable with transparent drawables. And
	 * also need to override onOverscrolled in listview with a blank method.
	 * 
	 * @param isSubClass true if the listView is sub class of ListView or GridView
	 * 
	 * @author Donald
	 */
	public static void removeOverscrollEffect(AbsListView listView, boolean isSubClass) {
		try {
			Context context = listView.getContext();
			Resources resources = context.getResources();
			Class superClass = listView.getClass().getSuperclass();
			if (isSubClass) {
				superClass = superClass.getSuperclass();
			}
			Field field = superClass.getDeclaredField("mEdgeGlowTop");
			field.setAccessible(true);
			Object edgeGlowTop = field.get(listView);
			Class edgeClass = edgeGlowTop.getClass();
			Field edgeDrawable = edgeClass.getDeclaredField("mEdge");
			edgeDrawable.setAccessible(true);
			edgeDrawable.set(edgeGlowTop, resources.getDrawable(R.drawable.trans_full));
			Field glowDrawable = edgeClass.getDeclaredField("mGlow");
			glowDrawable.setAccessible(true);
			glowDrawable.set(edgeGlowTop, resources.getDrawable(R.drawable.trans_full));
			field.set(listView, edgeGlowTop);

			Field fieldBottom = superClass.getDeclaredField("mEdgeGlowBottom");
			fieldBottom.setAccessible(true);
			Object edgeGlowBottom = fieldBottom.get(listView);
			Class edgeClassBottom = edgeGlowBottom.getClass();
			Field edgeDrawableBottom = edgeClassBottom.getDeclaredField("mEdge");
			edgeDrawableBottom.setAccessible(true);
			edgeDrawableBottom.set(edgeGlowBottom, resources.getDrawable(R.drawable.trans_full));
			Field glowDrawableBottom = edgeClassBottom.getDeclaredField("mGlow");
			glowDrawableBottom.setAccessible(true);
			glowDrawableBottom.set(edgeGlowBottom, resources.getDrawable(R.drawable.trans_full));
			fieldBottom.set(listView, edgeGlowBottom);
		} catch (Exception e) {
//			DmLog.d("ListViewUtil", e.getMessage());
		}
	}
}
