package hrider.ui.controls.xml;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

/**
 * Copyright (C) 2012 NICE Systems ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Igor Cher
 * @version %I%, %G%
 *          <p/>
 *          This class represents a custom XML view. All the drawings of different colors performed in this class.
 */
public class XmlView extends PlainView {

    //region Constants
    private final static HashMap<Pattern, Color> patternColors;
    private final static String TAG_PATTERN           = "(</?[a-zA-Z]*)\\s?>?";
    private final static String TAG_END_PATTERN       = "(/>)";
    private final static String TAG_ATTRIBUTE_PATTERN = "\\s(\\w*)\\=";
    private final static String TAG_ATTRIBUTE_VALUE   = "[a-zA-Z-]*\\=(\"[^\"]*\")";
    private final static String TAG_COMMENT           = "(<!--.*-->)";
    private final static String TAG_CDATA_START       = "(\\<!\\[CDATA\\[).*";
    private final static String TAG_CDATA_END         = ".*(]]>)";
    //endregion

    //region Constructor
    static {
        // NOTE: the order is important!
        patternColors = new HashMap<Pattern, Color>();
        patternColors.put(Pattern.compile(TAG_CDATA_START), new Color(128, 128, 128));
        patternColors.put(Pattern.compile(TAG_CDATA_END), new Color(128, 128, 128));
        patternColors.put(Pattern.compile(TAG_PATTERN), new Color(63, 127, 127));
        patternColors.put(Pattern.compile(TAG_ATTRIBUTE_PATTERN), new Color(127, 0, 127));
        patternColors.put(Pattern.compile(TAG_END_PATTERN), new Color(63, 127, 127));
        patternColors.put(Pattern.compile(TAG_ATTRIBUTE_VALUE), new Color(42, 0, 255));
        patternColors.put(Pattern.compile(TAG_COMMENT), new Color(63, 95, 191));
    }

    public XmlView(Element element) {

        super(element);

        // Set tab size to 4 (instead of the default 8)
        getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
    }
    //endregion

    //region Protected Methods
    @Override
    protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {

        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);

        Segment segment = getLineBuffer();

        SortedMap<Integer, Integer> startMap = new TreeMap<Integer, Integer>();
        SortedMap<Integer, Color> colorMap = new TreeMap<Integer, Color>();

        // Match all regexes on this snippet, store positions
        for (Map.Entry<Pattern, Color> entry : patternColors.entrySet()) {
            Matcher matcher = entry.getKey().matcher(text);
            while (matcher.find()) {
                startMap.put(matcher.start(1), matcher.end());
                colorMap.put(matcher.start(1), entry.getValue());
            }
        }

        // TODO: check the map for overlapping parts

        int i = 0;

        // Colour the parts
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
            int start = entry.getKey();
            int end = entry.getValue();

            if (i < start) {
                g.setColor(Color.black);
                doc.getText(p0 + i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, g, this, i);
            }

            g.setColor(colorMap.get(start));
            i = end;
            doc.getText(p0 + start, i - start, segment);
            x = Utilities.drawTabbedText(segment, x, y, g, this, start);
        }

        // Paint possible remaining text black
        if (i < text.length()) {
            g.setColor(Color.black);
            doc.getText(p0 + i, text.length() - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, g, this, i);
        }

        return x;
    }
    //endregion
}
