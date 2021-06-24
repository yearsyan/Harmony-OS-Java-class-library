package ohos.hiaivision.visionutil.text;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ohos.ai.cv.common.BoundingBox;
import ohos.ai.cv.common.CvPoint;
import ohos.ai.cv.common.CvRect;
import ohos.ai.cv.text.Text;
import ohos.ai.cv.text.TextBlock;
import ohos.ai.cv.text.TextElement;
import ohos.ai.cv.text.TextLine;
import ohos.ai.cv.text.TextParamKey;

public class TextUtility {
    private static final float DEFAULT_PROBABILITY = 0.0f;

    public static Text textFromBundle(Bundle bundle) {
        Text text = new Text();
        if (bundle != null) {
            text.setTextRect(rectToCvRect((Rect) bundle.getParcelable(TextParamKey.TEXT_RECT)));
            text.setValue(bundle.getString("value"));
            text.setPageLanguage(bundle.getInt(TextParamKey.TEXT_PAGE_LANGUAGE));
            text.setCornerPoints(pointsToCvPoints(bundle.getParcelableArray(TextParamKey.TEXT_CORNER_POINTS)));
            ArrayList parcelableArrayList = bundle.getParcelableArrayList(TextParamKey.TEXT_BLOCKS);
            if (parcelableArrayList != null) {
                ArrayList arrayList = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Parcelable parcelable = (Parcelable) it.next();
                    if (parcelable instanceof Bundle) {
                        arrayList.add(textBlockFromBundle((Bundle) parcelable));
                    }
                }
                text.setBlocks(arrayList);
            }
        }
        return text;
    }

    public Bundle textToBundle(Text text) {
        Bundle bundle = new Bundle();
        bundle.putString("value", text.getValue());
        List<TextBlock> blocks = text.getBlocks();
        if (blocks != null) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(blocks.size());
            for (TextBlock textBlock : blocks) {
                arrayList.add(textBlockToBundle(textBlock));
            }
            bundle.putParcelableArrayList(TextParamKey.TEXT_BLOCKS, arrayList);
        }
        bundle.putParcelable(TextParamKey.TEXT_RECT, cvRectToRect(text.getTextRect()));
        bundle.putParcelableArray(TextParamKey.TEXT_CORNER_POINTS, cvPointsToPoints(text.getCornerPoints()));
        bundle.putFloat(TextParamKey.TEXT_PROBABILITY, 0.0f);
        bundle.putInt(TextParamKey.TEXT_PAGE_LANGUAGE, text.getPageLanguage());
        return bundle;
    }

    private static TextBlock textBlockFromBundle(Bundle bundle) {
        TextBlock textBlock = new TextBlock();
        if (bundle != null) {
            Parcelable parcelable = bundle.getParcelable(TextParamKey.TEXT_RECT);
            if (parcelable instanceof Rect) {
                textBlock.setBoundingBox(rectToBoundingBox((Rect) parcelable));
            } else {
                textBlock.setBoundingBox(new BoundingBox());
            }
            textBlock.setValue(bundle.getString("value"));
            textBlock.setCornerPoints(pointsToCvPoints(bundle.getParcelableArray(TextParamKey.TEXT_CORNER_POINTS)));
            ArrayList parcelableArrayList = bundle.getParcelableArrayList(TextParamKey.TEXT_LINES);
            if (parcelableArrayList != null) {
                ArrayList arrayList = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Parcelable parcelable2 = (Parcelable) it.next();
                    if (parcelable2 instanceof Bundle) {
                        arrayList.add(textLineFromBundle((Bundle) parcelable2));
                    }
                }
                textBlock.setTextLines(arrayList);
            }
        }
        return textBlock;
    }

    private Bundle textBlockToBundle(TextBlock textBlock) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TextParamKey.TEXT_RECT, boundingBoxToRect(textBlock.getBoundingBox()));
        bundle.putString("value", textBlock.getValue());
        List<TextLine> textLines = textBlock.getTextLines();
        if (textLines != null) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(textLines.size());
            for (TextLine textLine : textLines) {
                arrayList.add(textLineToBundle(textLine));
            }
            bundle.putParcelableArrayList(TextParamKey.TEXT_LINES, arrayList);
        }
        bundle.putFloat(TextParamKey.TEXT_PROBABILITY, 0.0f);
        bundle.putParcelableArray(TextParamKey.TEXT_CORNER_POINTS, cvPointsToPoints(textBlock.getCornerPoints()));
        return bundle;
    }

    private static TextLine textLineFromBundle(Bundle bundle) {
        TextLine textLine = new TextLine();
        if (bundle != null) {
            textLine.setLineRect(rectToCvRect((Rect) bundle.getParcelable(TextParamKey.TEXT_RECT)));
            textLine.setValue(bundle.getString("value"));
            textLine.setLanguageType(bundle.getInt(TextParamKey.TEXT_LANGUAGE_TYPE));
            textLine.setCornerPoints(pointsToCvPoints(bundle.getParcelableArray(TextParamKey.TEXT_CORNER_POINTS)));
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("elements");
            if (parcelableArrayList != null) {
                ArrayList arrayList = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Parcelable parcelable = (Parcelable) it.next();
                    if (parcelable instanceof Bundle) {
                        arrayList.add(textElementFromBundle((Bundle) parcelable));
                    }
                }
                textLine.setElements(arrayList);
            }
        }
        return textLine;
    }

    private Bundle textLineToBundle(TextLine textLine) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TextParamKey.TEXT_RECT, cvRectToRect(textLine.getLineRect()));
        bundle.putString("value", textLine.getValue());
        List<TextElement> elements = textLine.getElements();
        if (elements != null) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(elements.size());
            for (TextElement textElement : elements) {
                arrayList.add(textElementToBundle(textElement));
            }
            bundle.putParcelableArrayList("elements", arrayList);
        }
        bundle.putFloat(TextParamKey.TEXT_PROBABILITY, 0.0f);
        bundle.putInt(TextParamKey.TEXT_LANGUAGE_TYPE, textLine.getLanguageType());
        bundle.putParcelableArray(TextParamKey.TEXT_CORNER_POINTS, cvPointsToPoints(textLine.getCornerPoints()));
        return bundle;
    }

    private static TextElement textElementFromBundle(Bundle bundle) {
        TextElement textElement = new TextElement();
        if (bundle != null) {
            textElement.setElementRect(rectToCvRect((Rect) bundle.getParcelable(TextParamKey.TEXT_RECT)));
            textElement.setValue(bundle.getString("value"));
            textElement.setCornerPoints(pointsToCvPoints(bundle.getParcelableArray(TextParamKey.TEXT_CORNER_POINTS)));
        }
        return textElement;
    }

    private Bundle textElementToBundle(TextElement textElement) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TextParamKey.TEXT_RECT, cvRectToRect(textElement.getElementRect()));
        bundle.putString("value", textElement.getValue());
        bundle.putFloat(TextParamKey.TEXT_PROBABILITY, 0.0f);
        bundle.putParcelableArray(TextParamKey.TEXT_CORNER_POINTS, cvPointsToPoints(textElement.getCornerPoints()));
        return bundle;
    }

    private static Point[] cvPointsToPoints(CvPoint[] cvPointArr) {
        Point[] pointArr = new Point[cvPointArr.length];
        for (int i = 0; i < cvPointArr.length; i++) {
            pointArr[i] = new Point(cvPointArr[i].x, cvPointArr[i].y);
        }
        return pointArr;
    }

    private static CvPoint[] pointsToCvPoints(Parcelable[] parcelableArr) {
        if (parcelableArr == null) {
            return new CvPoint[0];
        }
        Point[] pointArr = (Point[]) Arrays.copyOf(parcelableArr, parcelableArr.length, Point[].class);
        CvPoint[] cvPointArr = new CvPoint[pointArr.length];
        for (int i = 0; i < pointArr.length; i++) {
            cvPointArr[i] = new CvPoint(pointArr[i].x, pointArr[i].y);
        }
        return cvPointArr;
    }

    private static CvRect rectToCvRect(Rect rect) {
        if (rect == null) {
            return new CvRect();
        }
        return new CvRect(rect.top, rect.left, rect.bottom, rect.right);
    }

    private Rect cvRectToRect(CvRect cvRect) {
        return new Rect(cvRect.left, cvRect.top, cvRect.right, cvRect.bottom);
    }

    private static BoundingBox rectToBoundingBox(Rect rect) {
        BoundingBox boundingBox = new BoundingBox();
        if (rect != null) {
            boundingBox.setLeft(rect.left);
            boundingBox.setTop(rect.top);
            boundingBox.setHeight(Math.abs(rect.bottom - rect.top));
            boundingBox.setWidth(Math.abs(rect.right - rect.left));
        }
        return boundingBox;
    }

    private Rect boundingBoxToRect(BoundingBox boundingBox) {
        Rect rect = new Rect();
        if (boundingBox != null) {
            int left = boundingBox.getLeft();
            int top = boundingBox.getTop();
            rect.set(left, top, boundingBox.getWidth() + left, boundingBox.getHeight() + top);
        }
        return rect;
    }
}
