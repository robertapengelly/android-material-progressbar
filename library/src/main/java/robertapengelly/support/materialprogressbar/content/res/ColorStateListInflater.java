package robertapengelly.support.materialprogressbar.content.res;

import  android.content.res.ColorStateList;
import  android.content.res.Resources;
import  android.content.res.TypedArray;
import  android.graphics.Color;
import  android.support.content.res.GrowingArrayUtils;
import  android.support.graphics.ColorUtils;
import  android.util.AttributeSet;
import  android.util.StateSet;
import  android.util.Xml;

import  java.io.IOException;

import  org.xmlpull.v1.XmlPullParser;
import  org.xmlpull.v1.XmlPullParserException;

import  robertapengelly.support.materialprogressbar.R;

final class ColorStateListInflater {

    private static final int DEFAULT_COLOR = Color.RED;
    
    private ColorStateListInflater() {}
    
    /**
     * Creates a ColorStateList from an XML document using given a set of
     * {@link Resources} and a {@link Resources.Theme}.
     *
     * @param r      Resources against which the ColorStateList should be inflated.
     * @param parser Parser for the XML document defining the ColorStateList.
     * @param theme  Optional theme to apply to the color state list, may be
     *               {@code null}.
     * @return A new color state list.
     */
    public static ColorStateList createFromXml(Resources r, XmlPullParser parser, Resources.Theme theme)
        throws XmlPullParserException, IOException {
        
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        
        int type;
        
        //noinspection StatementWithEmptyBody
        while (((type = parser.next()) != XmlPullParser.START_TAG) && (type != XmlPullParser.END_DOCUMENT));
        
        if (type != XmlPullParser.START_TAG)
            throw new XmlPullParserException("No start tag found");
        
        return createFromXmlInner(r, parser, attrs, theme);
    
    }
    
    /**
     * Create from inside an XML document. Called on a parser positioned at a
     * tag in an XML document, tries to create a ColorStateList from that tag.
     *
     * @throws XmlPullParserException if the current tag is not &lt;selector>
     * @return A new color state list for the current tag.
     */
    private static ColorStateList createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs,
        Resources.Theme theme) throws XmlPullParserException, IOException {
        
        final String name = parser.getName();
        
        if (!name.equals("selector"))
            throw new XmlPullParserException(parser.getPositionDescription() + ": invalid color state list tag " + name);
        
        return inflate(r, parser, attrs, theme);
    
    }
    
    /** Fill in this object based on the contents of an XML "selector" element. */
    private static ColorStateList inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme)
        throws XmlPullParserException, IOException {
        
        final int innerDepth = parser.getDepth() + 1;
        
        int defaultColor = DEFAULT_COLOR;
        int depth, type;
        
        int[][] stateSpecList = new int[20][];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;
        
        while (((type = parser.next()) != XmlPullParser.END_DOCUMENT)
            && (((depth = parser.getDepth()) >= innerDepth) || (type != XmlPullParser.END_TAG))) {
            
            if ((type != XmlPullParser.START_TAG) || (depth > innerDepth) || !parser.getName().equals("item"))
                continue;
            
            final TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.ColorStateListItem);
            final int baseColor = a.getColor(R.styleable.ColorStateListItem_android_color, Color.MAGENTA);
            
            float alphaMod = 1.0f;
            
            if (a.hasValue(R.styleable.ColorStateListItem_android_alpha))
                alphaMod = a.getFloat(R.styleable.ColorStateListItem_android_alpha, alphaMod);
            else if (a.hasValue(R.styleable.ColorStateListItem_alpha))
                alphaMod = a.getFloat(R.styleable.ColorStateListItem_alpha, alphaMod);
            
            a.recycle();
            
            // Parse all unrecognized attributes as state specifiers.
            int j = 0;
            final int numAttrs = attrs.getAttributeCount();
            int[] stateSpec = new int[numAttrs];
            
            for (int i = 0; i < numAttrs; ++i) {
            
                final int stateResId = attrs.getAttributeNameResource(i);
                
                if ((stateResId != android.R.attr.color) && (stateResId != android.R.attr.alpha)
                    && (stateResId != R.attr.alpha))
                    // Unrecognized attribute, add to state set
                    stateSpec[j++] = (attrs.getAttributeBooleanValue(i, false) ? stateResId : -stateResId);
            
            }
            
            stateSpec = StateSet.trimStateSet(stateSpec, j);
            
            // Apply alpha modulation. If we couldn't resolve the color or
            // alpha yet, the default values leave us enough information to
            // modulate again during applyTheme().
            final int color = modulateColorAlpha(baseColor, alphaMod);
            
            if ((listSize == 0) || (stateSpec.length == 0))
                defaultColor = color;
            
            colorList = GrowingArrayUtils.append(colorList, listSize, color);
            stateSpecList = GrowingArrayUtils.append(stateSpecList, listSize, stateSpec);
            listSize++;
        
        }
        
        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize][];
        System.arraycopy(colorList, 0, colors, 0, listSize);
        System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);
        
        return new ColorStateList(stateSpecs, colors);
    
    }
    
    private static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(Color.alpha(color) * alphaMod));
    }
    
    private static TypedArray obtainAttributes(Resources res, Resources.Theme theme, AttributeSet set, int[] attrs) {
        return ((theme == null) ? res.obtainAttributes(set, attrs) : theme.obtainStyledAttributes(set, attrs, 0, 0));
    }

}