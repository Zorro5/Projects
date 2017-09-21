import java.awt.*;
import java.io.*;
//import com.sun.image.codec.jpeg.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.lang.Number;
import java.nio.*;
import java.lang.Object;

/**
 * 
 * Class ImageData is a base class which respresents image data and the methods
 * for producing the corresponding wavelet image, as well as methods to access
 * both of these datas.
 * </p>
 * 
 * @author L. Grewe
 * @version 0.0a Feb. 1999
 */

// Note: extends Component to inherit its createImage() method
class ImageData extends Component {
	boolean verbose = false;

	// File where data stored and format
	String filename = "";
	String format = "";

	// Num Rows, columns
	public int rows = 0, cols = 0;
	BufferedImage image;

	// image data
	public int data[]; // TIP: MAYBE CHANGING THIS TO USE java.awt.Image WOULD
	// MAKE YOUR CODING EASIER
	public float minDataRange = Float.MAX_VALUE;
	public float maxDataRange = Float.MIN_VALUE;

	// **METHODS: for image data*/
	int getData(int row, int col) {
		if (row < rows && col < cols)
			return data[(row * cols) + col];
		else
			return 0;
	}

	int getDataForDisplay(int row, int col) {
		if (row < rows && col < cols)
			return data[(row * cols) + col];
		else
			return 0;
	}

	void setData(int row, int col, int value) {
		data[(row * cols) + col] = (int) value;

	}

	/**
	 * Constructs a ImageData object using the specified by an instance of
	 * java.awt.Image, format, and size indicated by numberRows and
	 * numberColumns.
	 * 
	 * @param img
	 *            an Image object containing the data.
	 * @param DataFormat
	 *            the format of the data
	 * @param numberRows
	 *            the number of rows of data
	 * @param numberColumns
	 *            the number of columns of data
	 * @exception IOException
	 *                if there is an error during reading of the rangeDataFile.
	 */
	public ImageData(Image img, String DataFormat, int numberRows, int numberColumns) throws IOException {
		int pixel, red, green, blue, r, c;
		format = DataFormat;
		rows = numberRows;
		cols = numberColumns;
		PixelGrabber pg;

		// From the image passed retrieve the pixels by
		// creating a pixelgrabber and dump pixels
		// into the data[] array.
		data = new int[rows * cols];
		pg = new PixelGrabber(img, 0, 0, cols, rows, data, 0, cols);
		// SPECIAL NOTE: you could change so stores in java.awt.Image instead
		try {
			System.out.print(pg.grabPixels());
			// this actually gets the pixel data and puts it into the array.
		} catch (InterruptedException e) {
			System.err.println("interrupted waiting for pixels!");
			return;
		}

		// Convert the PixelGrabber pixels to greyscale
		// from the {Alpha, Red, Green, Blue} format
		// PixelGrabber uses.
		for (r = 0; r < rows; r++) {
			for (c = 0; c < cols; c++) {
				pixel = data[r * cols + c];
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				if (verbose)
					System.out.println("RGB: " + red + "," + green + "," + blue);
				data[r * cols + c] = (int) ((red + green + blue) / 3); // SPECIAL
				// NOTE:
				// This
				// sample
				// code
				// converts
				// RGB
				// image
				// to a
				// greyscale
				// one

				if (verbose)
					System.out.println("Pixel: " + (int) ((red + green + blue) / 3));
				minDataRange = Math.min(minDataRange, data[r * cols + c]);
				maxDataRange = Math.max(maxDataRange, data[r * cols + c]);
			}

		}

		// {{INIT_CONTROLS
		setBackground(java.awt.Color.white);
		setSize(0, 0);
		// }}
	}

	/**
	 * Constructs a ImageData object using the specified size indicated by
	 * numberRows and numberColumns that is EMPTY.
	 * 
	 * @param numberRows
	 *            the number of rows of data
	 * @param numberColumns
	 *            the number of columns of data
	 */
	public ImageData(int numberRows, int numberColumns) {

		rows = numberRows;
		cols = numberColumns;

	}

	/**
	 * Constructs a ImageData object using the specified size indicated by
	 * numberRows and numberColumns. Fill the data[] array with the information
	 * stored in the ImageData instance ID, from the 2D neighborhood starting at
	 * the upper-left coordinate (rStart,cStart)
	 * 
	 * @param numberRows
	 *            the number of rows of data
	 * @param numberColumns
	 *            the number of columns of data
	 * @param ID
	 *            image data to copy data from
	 * @param rStart,cStart
	 *            Start of Neighborhood copy
	 */
	public ImageData(int numberRows, int numberColumns, ImageData ID, int rStart, int cStart) {

		// saftey check: Retrieval in ID outside of boundaries
		if (ID.rows < (rStart + numberRows) || ID.cols < (cStart + numberColumns)) {
			rows = 0;
			cols = 0;
			return;
		}

		rows = numberRows;
		cols = numberColumns;

		// create data[] array.
		data = new int[rows * cols];

		// Copy data from ID.
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				data[i * cols + j] = ID.data[(rStart + i) * ID.cols + j + cStart];
				minDataRange = Math.min(minDataRange, data[i * cols + j]);
				maxDataRange = Math.max(maxDataRange, data[i * cols + j]);
			}

	}

	// METHODS

	// Brighten Method - Increases the value of the pixel by value X

	public void brighten(int x) {

		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] + x;
			if (data[i] >= 255)
				data[i] = 255;
		}

	}

	/**
	 * creates a java.awt.Image from the pixels stored in the array data using
	 * java.awt.image.MemoryImageSource
	 */

	// Creates the Output Image - using the modified pixel value

	public Image createImage() {
		int pixels[], t;
		pixels = new int[rows * cols];

		// translate the data in data[] to format needed
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++) {
				t = data[r * cols + c];
				if (t == 999) // due to reg. transformation boundaries produced
				{
					t = 0;
				} // see Transform.ApplyToImage() method
				if (t < 0) // due to processing
				{
					t = -t;
				} else if (t > 255) // due to processing
				{
					t = 255;
				}

				pixels[r * cols + c] = (255 << 24) | (t << 16) | (t << 8) | t;
				// note data is greyscale so red=green=blue above (alpha first)
			}

		// Now create Image using new MemoryImageSource
		return (super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));

	}

	public void saveToFile(String filename, Image img) throws IOException {

		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics g = bufferedImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		try {
			ImageIO.write(bufferedImage, "jpg", new File(filename));
		} catch (Exception e) {
		}

	}

	/**
	 * Stores the data image to a a file as COLOR raw image data format
	 */
	public void storeImage(String filename) throws IOException {

		int pixel, alpha, red, green, blue;

		// Open up file
		FileOutputStream file_output = new FileOutputStream(filename);
		DataOutputStream DO = new DataOutputStream(file_output);

		// Write out each pixel as integers

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++) {
				pixel = data[r * cols + c];
				red = pixel;
				green = pixel;
				blue = pixel;
				if (verbose)// verbose
				{
					System.out.println("value: " + (int) ((red + green + blue) / 3));
					System.out.println(" R,G,B: " + red + "," + green + "," + blue);
				}

				DO.writeByte(red);
				DO.writeByte(green);
				DO.writeByte(blue);
			}

		// flush Stream
		DO.flush();
		// close Stream
		DO.close();

	}

	// Negative Function

	public void negative() {
		int pixel, alpha, red, green, blue;
		int value = 255;
		int i = 0;

		System.out.println("Applying Negative algorithm:  " + rows * cols);
		for (i = 0; i < rows * cols; i++) {
			pixel = data[i];
			pixel = value - pixel;

			if (pixel < 0) // Pixel value should be in 0 and 255
				pixel = 0;

			data[i] = pixel;
			// Modifying the content of data[] with new pixel values

		}
		System.out.println(i);
	}

	// Threshold Function

	public void threshold(int value) {
		int pixel;
		int i = 0;

		System.out.println(" Applying Threshold Function:  " + rows * cols);

		for (i = 0; i < rows * cols; i++) {
			pixel = data[i];

			if (pixel < value)
				pixel = 0; // Pixel value should be in 0 to 255
			else
				pixel = 255;
			data[i] = pixel;
			// Modifying the content of data[] with new pixel values
		}
		System.out.println(i);
	}

	// Edge Detection Function

	public void edgeDetection() {
		int[] input;
		int[] output;
		double[] direction;
		int seek;
		int templateSize = 3;
		float[] template = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
		;

		float[] GY = new float[cols * rows];
		float[] GX = new float[cols * rows];
		int[] total = new int[cols * rows];
		seek = 0;
		int sum = 0;
		int max = 0;

		input = new int[rows * cols];
		output = new int[rows * cols];
		direction = new double[rows * cols];
		for (int r = 0; r < data.length; r++) {
			input[r] = data[r];
		}
		for (int x = (templateSize - 1) / 2; x < cols - (templateSize + 1) / 2; x++) {
			seek++;
			for (int y = (templateSize - 1) / 2; y < rows - (templateSize + 1) / 2; y++) {
				sum = 0;

				for (int x1 = 0; x1 < templateSize; x1++) {
					for (int y1 = 0; y1 < templateSize; y1++) {
						int x2 = (x - (templateSize - 1) / 2 + x1);
						int y2 = (y - (templateSize - 1) / 2 + y1);
						float value = (input[y2 * cols + x2] & 0xff) * (template[y1 * templateSize + x1]);
						sum += value;
					}
				}
				GY[y * cols + x] = sum;
				for (int x1 = 0; x1 < templateSize; x1++) {
					for (int y1 = 0; y1 < templateSize; y1++) {
						int x2 = (x - (templateSize - 1) / 2 + x1);
						int y2 = (y - (templateSize - 1) / 2 + y1);
						float value = (input[y2 * cols + x2] & 0xff) * (template[x1 * templateSize + y1]);
						sum += value;
					}
				}
				GX[y * cols + x] = sum;

			}
		}
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				total[y * cols + x] = (int) Math
						.sqrt(GX[y * cols + x] * GX[y * cols + x] + GY[y * cols + x] * GY[y * cols + x]);
				direction[y * cols + x] = Math.atan2(GX[y * cols + x], GY[y * cols + x]);
				if (max < total[y * cols + x])
					max = total[y * cols + x];
			}
		}
		float ratio = (float) max / 255;
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				sum = (int) (total[y * cols + x] / ratio);
				output[y * cols + x] = 0xff000000 | ((int) sum << 16 | (int) sum << 8 | (int) sum);
			}
		}
		seek = cols;
		for (int r = 0; r < data.length; r++) {
			data[r] = output[r];
		}
	}

	// Contrast Function

	public void contrast(double contValue) {
		int i = 0;
		int pixel;
		Double temp;
		double factor = (259 * (contValue + 255)) / (255 * (259 - contValue));

		System.out.println("Applying Contrast Function:  " + rows * cols + "            " + factor);

		for (i = 0; i < rows * cols; i++) {
			pixel = data[i];

			temp = (pixel - 0.5) * contValue + 0.5;
			pixel = temp.intValue();

			if (pixel < 0)
				pixel = 0;
			else if (pixel > 255)
				pixel = 255; // Pixel value should be in 0 and 255

			data[i] = pixel; // Modifying the content of data[] with new pixel
			// values

		}
		System.out.println(i);

	}

	// {{DECLARE_CONTROLS
	// }}
}// End ImageData