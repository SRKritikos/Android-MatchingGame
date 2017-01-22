package sl.on.ca.comp208.imagematcher.model;

import java.util.ArrayList;
import java.util.List;

import sl.on.ca.comp208.imagematcher.R;

/**
 * Created by srostantkritikos06 on 1/16/2017.
 */
public class ImageIds {
    List<Integer> imageIds;

    public ImageIds() {
        this.imageIds = new ArrayList<>();
        imageIds.add(R.drawable.blue_flowers);
        imageIds.add(R.drawable.blue_flowers);
        imageIds.add(R.drawable.green_flowers);
        imageIds.add(R.drawable.green_flowers);
        imageIds.add(R.drawable.orange_flowers);
        imageIds.add(R.drawable.orange_flowers);
        imageIds.add(R.drawable.pink_flowers);
        imageIds.add(R.drawable.pink_flowers);
        imageIds.add(R.drawable.purple_flowers);
        imageIds.add(R.drawable.purple_flowers);
        imageIds.add(R.drawable.red_flowers);
        imageIds.add(R.drawable.red_flowers);
        imageIds.add(R.drawable.white_flowers);
        imageIds.add(R.drawable.white_flowers);
        imageIds.add(R.drawable.yellow_flowers);
        imageIds.add(R.drawable.yellow_flowers);
    }

    public List<Integer> getImageIds() {
        return imageIds;
    }

}
