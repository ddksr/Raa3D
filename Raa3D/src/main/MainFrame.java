/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */

/*
 * Libraries used are TWL, slick-util, xpp3 and LWJGL.
 * Their licenses can be seen in one place by:
 *  - compiling and running the program and selecting the "Licensing" button or
 *  - by searching for String creditsString in this file.
 * The licenses should also be accessible online and some are included with the library folders.
 */
package main;
/**
 *@author Simon Žagar, 63090355
*/

import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glMultMatrix;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilMask;
import static org.lwjgl.opengl.GL11.glStencilOp;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static tools.Tools.allocFloats;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.color.ColorSpace;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import models.Bubbles;
import models.ObjOrPlyModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import raa.pin.PinNote;
import raa.pin.PinPanel;
import tools.Quaternion;
import tools.Vector;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.FileSelector;
import de.matthiasmann.twl.FileSelector.Callback;
import de.matthiasmann.twl.FolderBrowser;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ProgressBar;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.Scrollbar;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.DefaultEditFieldModel;
import de.matthiasmann.twl.model.EditFieldModel;
import de.matthiasmann.twl.model.JavaFileSystemModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLDynamicImage;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLTexture;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.textarea.StyleSheet;
import de.matthiasmann.twl.textarea.TextAreaModel;
import de.matthiasmann.twl.theme.ThemeManager;

import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * @author Simon �agar, 63090355
 *@version 0.4
 *@since 0.1
 */
public class MainFrame extends Widget{
    static class NeckVeinsSettings implements Serializable{//similar to global variables except it can be saved
        private int resWidth, resHeight, bitsPerPixel, frequency;
        private boolean isFpsShown, fullscreen, stereoEnabled;
        private int stereoValue=0;
    }
    private static void saveSettings(){
        boolean saveSuccesful=false;
        String saveFileName=title+".save";
        try{FileOutputStream fos = new FileOutputStream(saveFileName, false);
            try{ObjectOutputStream saveOutStream=new ObjectOutputStream(fos);
                saveOutStream.writeObject(settings);
                saveOutStream.close();
                saveSuccesful=true;
            }catch(IOException ee){}
        }catch(FileNotFoundException e){}
        if(!saveSuccesful) System.out.println("SaveUnsuccesful");
    }
    private static boolean loadSettings(){
        boolean loadSuccesful=false;
        String saveFileName=title+".save";
        try{
            FileInputStream fis = new FileInputStream(saveFileName);
            try{
                ObjectInputStream saveInStream=new ObjectInputStream(fis);
                try{Object aSave=saveInStream.readObject();
                    if(aSave instanceof NeckVeinsSettings){
                        settings=(NeckVeinsSettings)aSave;
                        loadSuccesful=true;
                    }
                }catch(ClassNotFoundException eee){}
            }catch(IOException ee){}
        }catch(FileNotFoundException e){}
        if(!loadSuccesful){
            System.out.println("Loading settings file failed. Using default/current settings instead.");
            return false;
        }
        else{
            System.out.println("Loading settings file succeded.");
            return true;
        }
    }
    
    static double MAX_RAY_DISTANCE = 10.; // set to lower when ray works
    
    static final int LMB = 0;
    static final int RMB = 1;
    static final int MWB = 2;
    
    private static NeckVeinsSettings settings;
    public static MainFrame gameUI;

    public static boolean loadAsyncModel;
    
    
    //Widgets
    private static ThemeManager themeManager;
    private Button open;
    private ToggleButton pinPanelToggleButton;
    private Button openPinButton;
    private Button newPinButton;
    private Button savePinButton;
    private Button saveAsPinButton;
    private ToggleButton additionalContentToggleButton;
    
    private Button displayModesButton, okayVideoSetting, cancelVideoSetting;
    private Button prtscr;
    private Button exit;
    private ToggleButton help;
    private ToggleButton credits;
    private ToggleButton stereoToggleButton;
    private Scrollbar stereoScrollbar;
    private ScrollPane helpScrollPane;
    private TextArea helpTextArea;
    private SimpleTextAreaModel stamHelp, stamCredits;
    private FileSelector fileSelector;
    
    /**image***/
    private static ImageWidget imageWidget;
    /**********/
    public static ProgressBar progressBar;
    
    private ListBox displayModeListBox;
    private de.matthiasmann.twl.ToggleButton fullscreenToggle;

    private static Widget draggableWidget;
    private TextArea msgBoxContent;
    private TextArea msgBoxTitle;
    private EditField msgBoxInput;
    private Button msgBoxOkButton;
    private Button msgBoxCloseButton;
    private Button msgBoxCancelButton;
    
    // Pin note buttons
    private ToggleButton modeToggleButton;
    
    private ToggleButton pinTypToggleButton;
    private ToggleButton pinImgToggleButton;
    private ToggleButton pinAbsToggleButton;
    private ToggleButton pinTxtToggleButton;
    
    // Pin note iteraction
    private static ScrollPane pinItrPane;
    private EditField textPinField;
    private Label xLabelField;
    private EditField xPinField;
    private Label yLabelField;
    private EditField yPinField;
    private Label zLabelField;
    private EditField zPinField;
    private Button pinItrOkButton;
    private Button pinItrDelButton;
    private Button pinItrCancelButton;
    private PinNote note;
    private TextArea pinItrBg;

    private static boolean dialogOpened = false;
    private static boolean menuOpened = false;
    //parameters
    static float fovy=45;
    static float zNear=1;
    static float zFar=10000;
    static String title="Raa3D";
    ////global variables
    //GUI and display modes
    public static GUI gui;
    static LWJGLRenderer renderer;
    static DisplayMode[] displayModes;
    static DisplayMode currentDisplayMode;
    static String[]displayModeStrings;
    static int selectedResolution=-1;
    //veins model and shaders
    static ObjOrPlyModel openedModel;
    static Quaternion currentModelOrientation, addedModelOrientation;
    static int[] shaderPrograms, vertexShaders, fragmentShaders;
    static final int NUMBER_OF_SHADER_PROGRAMS=9;
    static int activeShaderProgram=6;
    //HUD
    static Texture rotationCircle, circleGlow, movementCircle, rotationElipse, movementElipse, ellipseGlow, bubbleTexture;
    //window variables
    private static boolean isRunning;
    private static long timePastFrame, fps, timePastFps, fpsToDisplay;
    //camera pose
    static Quaternion cameraOrientation;
    static float cameraX=0, cameraY=0, cameraZ=0, cameraMoveSpeed=1.667f;
    //mouse position
    static int mouse_x = Mouse.getX();
    static int mouse_y = Mouse.getY();
    //zoom
    static double zoom = 1;

    static double cameraRotationSpeed=(float)(72*Math.PI/180/60);
    //variables for rotating the veins
    static double[] screenPlaneInitialUpperLeft, screenPlaneInitialUpperRight, screenPlaneInitialLowerLeft, screenPlaneInitialLowerRight;
    static double veinsRadius;
    static double[] veinsGrabbedAt=null;
    static int clickedOn=0;
    static float rotationCircleAngle=0, rotationCircleDistance=0, ellipseSide=0;
    //constants
    static final int CLICKED_ON_NOTHING=0, CLICKED_ON_VEINS_MODEL=1, CLICKED_ON_ROTATION_CIRCLE=2, CLICKED_ON_MOVE_CIRCLE=3,
    CLICKED_ON_ROTATION_ELLIPSE=4, CLICKED_ON_MOVE_ELLIPSE=5, CLICKED_ON_BUTTONS=6;
    static final float ellipsef=1.1180339887498948482045868343656f;
    
    // PinPanel related variables
    private static boolean loadingPinPanel = false;
    private static boolean loadingImage = false;
    private static PinPanel pinPanel;
    
    
    //private static TextArea keyboardInputText = null;
    //private static String inputContent = null;
    private static boolean inputTextMode = false;
    
    // String containing default path of the model
    private static String defaultPath = null;
    private static String modelName = null;
    
    private static boolean editMode = false;
    
    private static int pinNoteType = PinNote.TEXT_TYPE;
    
    public static boolean clickAnyWhereOnModel = false;
    public static boolean pinsVisible = false;
    
    private static float[] lastRay = null;

    // Set the source of light
    private static float[] lightOrigin = new float[]{0.0f, 1000.0f, 0.0f , 0.0f};
    private static int lightOriginAngleX = -90;
    private static int lightOriginAngleY = 0;
    private static int lightDistance = 1000;
    
    public static ArrayList<float[]> bubbles_absolutePoints=new ArrayList<float[]>();
    public static ArrayList<float[]> bubbles_images=new ArrayList<float[]>();
    public static ArrayList<float[]> bubbles_text=new ArrayList<float[]>();
    
    /**
     * @since 0.4
     * @version 0.4
     */
    public MainFrame(){
        // Create tmp
        File tmp = new File("tmp");
        if(! tmp.exists()) {
            tmp.mkdir();
        }
        
        
        lastRay = new float[] {0f, 0f ,0f};
        
        fileSelector = new FileSelector();
        fileSelector.setTheme("fileselector");
        fileSelector.setVisible(false);
        de.matthiasmann.twl.model.JavaFileSystemModel fsm= JavaFileSystemModel.getInstance();
        fileSelector.setFileSystemModel(fsm);
        Callback cb = new Callback() {
            @Override
            public void filesSelected(Object[] files) {
                if (! loadingImage) {
                    setButtonsEnabled(true);
                    fileSelector.setVisible(false);
                    File file= (File)files[0];
                    String path = file.getAbsolutePath();
                    System.out.println("\nOpening file: "+path);
                    dialogOpened = false;
                    if (loadingPinPanel) {
                        loadPinPanel(path);
                        loadingPinPanel = false;
    
                        initPinButtonsEnabled();
                    }
                    else {
                        defaultPath = path.substring(0, path.lastIndexOf(File.separatorChar)) + File.separatorChar;
                        modelName = file.getName();
                        infoBox("Info", "Loading model ... ");
                        loadAsyncModel = true;
                        String ppFile = path + "." + PinPanel.EXT;
                        if(new File(ppFile).exists()) {
                            System.out.println("Pin panel for model " + modelName + " exists. Loading pin panel ...");
                            loadPinPanel(ppFile);
                        }
                        else pinPanel = null; // set to null!
                        msgBoxDestroy();
    
                        // TODO: open pin panel if exists
                        initPinButtonsEnabled();
                    }
                }
                else {
                    setButtonsEnabled(true);
                    fileSelector.setVisible(false);
                    File file= (File)files[0];
                    String path = file.getAbsolutePath();
                    
                    note = PinNote.newImgNote(lastRay[0], lastRay[1], lastRay[2], path);
                    note.setAbsImageLocation(path);
                    showImage(note, true, true);
                    
                    loadingImage = false;
                }
            }
            @Override
            public void canceled() {
                setButtonsEnabled(true);
                fileSelector.setVisible(false);
                dialogOpened = false;
            }
        };
        fileSelector.addCallback(cb);
        add(fileSelector);
        
        
        open = new Button("Open model ...");
        open.setTheme("button");
        open.setTooltipContent("Open the dialog with the file chooser to select an .obj file.");
        open.addCallback(new Runnable(){
           @Override
        public void run(){
               openAFile();
           }
        });
        add(open);
        /*
         * progressbar
         */
        progressBar = new ProgressBar();
        add(progressBar);
        
        modeToggleButton = new ToggleButton("Edit mode");
        modeToggleButton.setTheme("togglebutton");
        modeToggleButton.setTooltipContent("Select mode");
        modeToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               editMode = modeToggleButton.isActive();
               pinTypToggleButton.setEnabled(openedModel != null && pinPanel != null && editMode);
           }
        });
        add(modeToggleButton);
        
        additionalContentToggleButton = new ToggleButton("Show pin notes");
        additionalContentToggleButton.setTheme("togglebutton");
        additionalContentToggleButton.setTooltipContent("Select mode");
        additionalContentToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               pinsVisible = additionalContentToggleButton.isActive();
           }
        });
        add(additionalContentToggleButton);
        
        
             
        prtscr = new Button("Screen shot ...");
        prtscr.setTheme("button");
        prtscr.setTooltipContent("Create screenshot.");
        prtscr.addCallback(new Runnable(){
           /**
            * Create screen shoot of current view
            */
           public void run(){
               GL11.glReadBuffer(GL11.GL_FRONT);
               int width = Display.getDisplayMode().getWidth();
               int height= Display.getDisplayMode().getHeight();
               int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
               ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
               GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);                             
               BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
               String prtscr_filename = "screenshot.png";
               File outfile =  new File(prtscr_filename);
               
               for(int x = 0; x < width; x++) { 
                   for(int y = 0; y < height; y++) {
                       int i = (x + (width * y)) * bpp;
                       int r = buffer.get(i) & 0xFF;
                       int g = buffer.get(i + 1) & 0xFF;
                       int b = buffer.get(i + 2) & 0xFF;
                       image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                   }
               }

               try {
                   //writePMImageToFile(image, "C:\\slike\\mojaslika.pm");
                   ImageIO.write(image, "PNG", outfile); // "PNG" or "JPG"
                   alertBox("Image " + prtscr_filename + " was saved.", "Image saved.");
               } catch (IOException e) { 
                   e.printStackTrace(); 
               }        
           }
           

        });
        add(prtscr);
        
        
        pinInit();
        
        
        
        exit = new Button("Exit");
        exit.setTheme("button");
        exit.setTooltipContent("Terminates this program.");
        exit.addCallback(new Runnable(){
           @Override
           public void run(){
               safeExit();
           }
        });
        add(exit);
        
        stereoScrollbar = new Scrollbar(Scrollbar.Orientation.HORIZONTAL);
        stereoScrollbar.setTheme("hscrollbar");
        stereoScrollbar.setTooltipContent("Sets the distance between eyes.");
        stereoScrollbar.setMinMaxValue(-1000, 1000);
        stereoScrollbar.setValue(settings.stereoValue);
        stereoScrollbar.addCallback(new Runnable(){
            @Override
            public void run(){
                settings.stereoValue=stereoScrollbar.getValue();
            }
        });
        add(stereoScrollbar);
        
        stereoToggleButton = new ToggleButton("Stereo (3D)");
        stereoToggleButton.setTheme("togglebutton");
        stereoToggleButton.setTooltipContent("Toggles interlaced 3D picture. Requires an appropriate display.");
        stereoToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
                   settings.stereoEnabled=true;
                   stereoScrollbar.setVisible(true);
                   invalidateLayout();
               if(stereoToggleButton.isActive()){
               }else{
                   settings.stereoEnabled=false;
                   stereoScrollbar.setVisible(false);
                   invalidateLayout();
               }
           }
        });
        add(stereoToggleButton);
        if(settings.stereoEnabled){
            stereoScrollbar.setVisible(true);
            stereoToggleButton.setActive(true);
        }else{
            stereoScrollbar.setVisible(false);
            stereoToggleButton.setActive(false);
        }
        
        help = new ToggleButton("Help");
        help.setTheme("togglebutton");
        help.setTooltipContent("Shows controls.");
        help.addCallback(new Runnable(){
           @Override
        public void run(){
               if(help.isActive()){
                   helpTextArea.setModel(stamHelp);
                   helpScrollPane.setVisible(true);
                   setButtonsEnabled(false);
                   help.setEnabled(true);
               }else{
                   helpScrollPane.setVisible(false);
                   setButtonsEnabled(true);
               }
           }
        });
        add(help);
        
        credits = new ToggleButton("Licensing");
        credits.setTheme("togglebutton");
        credits.setTooltipContent("Shows authorship and licensing information.");
        credits.addCallback(new Runnable(){
           @Override
        public void run(){
               if(credits.isActive()){
                   helpTextArea.setModel(stamCredits);
                   helpScrollPane.setVisible(true);
                   setButtonsEnabled(false);
                   credits.setEnabled(true);
               }else{
                   helpScrollPane.setVisible(false);
                   setButtonsEnabled(true);
               }
           }
        });
        add(credits);
        
        String helpString="The Q, W, E, A, S and D keyboard buttons are used to look arround (rotate the camera):\n" +
        "   W - looks up\n   S - looks down\n   A - looks to the left\n   D - looks to the right\n" +
        "   Q - rotates the camera counterclockwise\n   E - rotates the camera clockwise\n" +
        "You can also use the ellipse on the right for more intuitive or finer rotations.\n\n" +
        "The arows keys and the R and F keyboard buttons are used to move (translate) the camera:\n" +
        "   Up Arrow - moves camera forward\n   Down Arrow - moves camera backwards\n" +
        "   Left Arrow - moves the camera left\n   Right Arrow - moves the camera right\n" +
        "   R - moves the camera upwards\n   F - moves the camera downwards\n" +
        "You can also use the ellipse on the right for more intuitive or finer movements.\n\n" +
        "You can scroll the mouse wheel to move closer or further away from the model.\n" +
        "You can hold left or right mouse button while moving mouse.\n" +
        "You can click on an invisible sphere around the model and drag to rotate it.\n\n" +
        "You may change how the model is rendered with the following buttons:\n" +
        "   0 - use a fixed function pipeline\n" +
        "   1 - use a simple shader\n" +
        "   2 - like 1, but uses interpolated normals\n" +
        "   3 - like 2, but adds ambient light\n" +
        "   4 - like 3, but adds a specular component using Blinn�Phong model\n" +
        "   5 - like 3, but adds a specular component using Phong model (default)\n" +
        "   9 - toggles wireframe mode\n\n" +
        "You may apply a level of surface subdivision with the + button . Or switch back with the - button.\n" +
        "Maximal surface subdivision level that this application allows is 3. The starting one is 0.\n" +
        "Disclamer: Switching to a higher subdivision level for the first time constructs a new mesh.\n" +
        "Each such construction lasts about 6 times longer and is more likely to crash this application.\n\n" +
        "You can also activate 3D rendering for displays that support interlaced stereo.\n" +
        "The slider that appears below the \"Stereo (3D)\" button changes the distance between the eyes.\n" +
        "If the picture drawn for the right eye is shown to your left eye and vice versa, \n" +
        "try moving the slider on the other side of it's middle point.";
        String creditsString="Author(s):\n" +
                " - Simon �agar\n" +
                " - 3rd party libraries used, their respective licenses can be seen below.\n\n" +
                "This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.\n" +
                "To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/\n" +
                " or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.\n" +
                "\n" +
                "This software uses LWJGL (Lightweight Java Game Library).\n" +
                "LWJGL's license is quoted below between the two lines drawn with minus signs:\n" +
                "-------------------------------------- LWJGL License: --------------------------------------\n" +
                "\"Copyright (c) 2002-2008 Lightweight Java Game Library Project\n" +
                "All rights reserved.\n\n" +
                "Redistribution and use in source and binary forms, with or without\n" +
                "modification, are permitted provided that the following conditions are\n" +
                "met:\n\n" +
                "* Redistributions of source code must retain the above copyright\n" +
                "  notice, this list of conditions and the following disclaimer.\n\n" +
                "* Redistributions in binary form must reproduce the above copyright\n" +
                "  notice, this list of conditions and the following disclaimer in the\n" +
                "  documentation and/or other materials provided with the distribution.\n\n" +
                "* Neither the name of 'Light Weight Java Game Library' nor the names of\n" +
                "  its contributors may be used to endorse or promote products derived\n" +
                "  from this software without specific prior written permission.\n\n" +
                "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS\n" +
                "\"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED\n" +
                "TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR\n" +
                "PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR\n" +
                "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,\n" +
                "EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,\n" +
                "PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\n" +
                "PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF\n" +
                "LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
                "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\"\n"+
                "--------------------------------------------------------------------------------------------\n\n\n\n"+
                
                "This software uses Slick-Util library.\n" +
                "Slick-Util library license is quoted below between the two lines drawn with minus signs:\n" +
                "-------------------------------- Slick-Util library License: --------------------------------\n" +
                "\"Copyright (c) 2007, Slick 2D\n\n" +
                "All rights reserved.\n\n" +
                "Redistribution and use in source and binary forms, with or without\n" +
                "modification, are permitted provided that the following conditions are\n" +
                "met:\n\n" +
                "* Redistributions of source code must retain the above copyright\n" +
                "  notice, this list of conditions and the following disclaimer.\n\n" +
                "* Redistributions in binary form must reproduce the above copyright\n" +
                "  notice, this list of conditions and the following disclaimer in the\n" +
                "  documentation and/or other materials provided with the distribution.\n\n" +
                "* Neither the name of Slick 2D nor the names of\n" +
                "  its contributors may be used to endorse or promote products derived\n" +
                "  from this software without specific prior written permission.\n\n" +
                "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS\n" +
                "\"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED\n" +
                "TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR\n" +
                "PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR\n" +
                "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,\n" +
                "EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,\n" +
                "PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\n" +
                "PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF\n" +
                "LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
                "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\"\n"+
                "--------------------------------------------------------------------------------------------\n\n\n\n"+
                
                "This software uses TWL (Themable Widget Library).\n" +
                "TWL's license is quoted below between the two lines drawn with minus signs:\n" +
                "--------------------------------------- TWL License: ---------------------------------------\n" +
                "\"Copyright (c) 2008-2009, Matthias Mann\n\n" +
                "All rights reserved.\n\n" +
                "Redistribution and use in source and binary forms, with or without\n" +
                "modification, are permitted provided that the following conditions are met:\n\n" +
                "     * Redistributions of source code must retain the above copyright notice,\n" +
                "       this list of conditions and the following disclaimer.\n" +
                "     * Redistributions in binary form must reproduce the above copyright\n" +
                "       notice, this list of conditions and the following disclaimer in the\n" +
                "       documentation and/or other materials provided with the distribution.\n" +
                "     * Neither the name of Matthias Mann nor the names of its contributors may\n" +
                "       be used to endorse or promote products derived from this software\n" +
                "       without specific prior written permission.\n\n" +
                "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS\n" +
                "\"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT\n" +
                "LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR\n" +
                "A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR\n" +
                "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,\n" +
                "EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,\n" +
                "PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\n" +
                "PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF\n" +
                "LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
                "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\"\n" +
                "--------------------------------------------------------------------------------------------\n\n\n"+
                "TWL uses XML Pull Parser 3 (XPP3).\n" +
                "It's license requests the following two lines to be quoted:\n" +
                "  \"This product includes software developed by the Indiana University \n" +
                "  Extreme! Lab (http://www.extreme.indiana.edu/).\"\n\n" +
                "XPP3's license is quoted below between the two lines drawn with minus signs:\n" +
                "--------------------------------------- XPP3 License: ---------------------------------------\n" +
                "\"Indiana University Extreme! Lab Software License\n\n" +
                "Version 1.1.1\n\n" +
                "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved.\n\n" +
                "Redistribution and use in source and binary forms, with or without\n" +
                "modification, are permitted provided that the following conditions are met:\n\n" +
                "1. Redistributions of source code must retain the above copyright notice, \n" +
                "   this list of conditions and the following disclaimer.\n\n" +
                "2. Redistributions in binary form must reproduce the above copyright \n" +
                "   notice, this list of conditions and the following disclaimer in \n" +
                "   the documentation and/or other materials provided with the distribution.\n\n" +
                "3. The end-user documentation included with the redistribution, if any, \n" +
                "   must include the following acknowledgment:\n\n" +
                "  \"This product includes software developed by the Indiana University \n" +
                "  Extreme! Lab (http://www.extreme.indiana.edu/).\"\n\n" +
                "Alternately, this acknowledgment may appear in the software itself, \n" +
                "if and wherever such third-party acknowledgments normally appear.\n\n" +
                "4. The names \"Indiana Univeristy\" and \"Indiana Univeristy Extreme! Lab\"\n" +
                "must not be used to endorse or promote products derived from this \n" +
                "software without prior written permission. For written permission, \n" +
                "please contact http://www.extreme.indiana.edu/.\n\n" +
                "5. Products derived from this software may not use \"Indiana Univeristy\" \n" +
                "name nor may \"Indiana Univeristy\" appear in their name, without prior \n" +
                "written permission of the Indiana University.\n\n" +
                "THIS SOFTWARE IS PROVIDED \"AS IS\" AND ANY EXPRESSED OR IMPLIED\n" +
                "WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF\n" +
                "MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n" +
                "IN NO EVENT SHALL THE AUTHORS, COPYRIGHT HOLDERS OR ITS CONTRIBUTORS\n" +
                "BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
                "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF\n" +
                "SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR\n" +
                "BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,\n" +
                "WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR\n" +
                "OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF\n" +
                "ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\"\n" +
                "--------------------------------------------------------------------------------------------\n";
        
        
        
        
        
        helpTextArea = new TextArea();
        helpTextArea.setTheme("textarea");
        stamHelp=new SimpleTextAreaModel(helpString);
        stamCredits=new SimpleTextAreaModel(creditsString);
        helpTextArea.setModel(stamHelp);
        
        helpScrollPane= new ScrollPane();
        helpScrollPane.setTheme("scrollpane");
        helpScrollPane.setVisible(false);
        add(helpScrollPane);
        helpScrollPane.setContent(helpTextArea);
        
        displayModesButton = new Button("Display Modes...");
        displayModesButton.setTheme("button");
        displayModesButton.setTooltipContent("Open the list with the available display modes.");
        displayModesButton.addCallback(new Runnable(){
           @Override
        public void run(){
               listDisplayModes();
           }
        });
        add(displayModesButton);
        
        okayVideoSetting = new Button("Okay");
        cancelVideoSetting = new Button("Cancel");
        fullscreenToggle = new ToggleButton("Toggle Fullscreen");
        
        okayVideoSetting.setVisible(false);
        cancelVideoSetting.setVisible(false);
        fullscreenToggle.setVisible(false);
        
        okayVideoSetting.setTheme("button");
        cancelVideoSetting.setTheme("button");
        fullscreenToggle.setTheme("togglebutton");
        okayVideoSetting.addCallback(new Runnable(){
            @Override
            public void run() {
                confirmVideoSetting();
            }
        });
        cancelVideoSetting.addCallback(new Runnable(){
            @Override
            public void run() {
                cancelVideoSetting();
            }
        });
        fullscreenToggle.addCallback(new Runnable(){
            @Override
            public void run() {
                if(fullscreenToggle.isActive()){
                    try {
                        Display.setFullscreen(true);
                        Display.setVSyncEnabled(true);
                        settings.fullscreen=true;
                    } catch(LWJGLException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        settings.fullscreen=false;
                        Display.setFullscreen(false);
                    } catch(LWJGLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if(settings.fullscreen){
            try {
                Display.setFullscreen(true);
                Display.setVSyncEnabled(true);
                fullscreenToggle.setActive(true);
            } catch(LWJGLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Display.setFullscreen(false);
                fullscreenToggle.setActive(false);
            } catch(LWJGLException e) {
                e.printStackTrace();
            }
        }
        add(okayVideoSetting);
        add(cancelVideoSetting);
        add(fullscreenToggle);
        
        displayModeListBox = new ListBox<String>();
        displayModeListBox.setTheme("listbox");
        displayModeListBox.setVisible(false);
        add(displayModeListBox);
        
        int i=0;
        selectedResolution=-1;
        for(DisplayMode displayMode:displayModes){
            if (displayMode.getWidth() == currentDisplayMode.getWidth() && displayMode.getHeight() == currentDisplayMode.getHeight() && displayMode.getBitsPerPixel() == currentDisplayMode.getBitsPerPixel() && displayMode.getFrequency() == currentDisplayMode.getFrequency()) {
                selectedResolution=i;
            }
            displayModeStrings[i++]=String.format("%1$5d x%2$5d x%3$3dbit x%4$3dHz", displayMode.getWidth(), displayMode.getHeight(),displayMode.getBitsPerPixel(),displayMode.getFrequency());
        }
        
        SimpleChangableListModel<String> scListModel= new SimpleChangableListModel<String>();
        for(String str:displayModeStrings){
            scListModel.addElement(str);
        }
        
        displayModeListBox.setModel(scListModel);
        if(selectedResolution!=-1)displayModeListBox.setSelected(selectedResolution);
    }
    
    
    
    protected void safeExit() {
        System.out.println("Safe exit");
        if (pinPanel != null) {
            try {
                boolean success = pinPanel.close();
                if (success) {
                    exitProgram(0);
                }
                else {
                    confirmBox("Unsaved changes", "Are you sure?", new Runnable() {
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                pinPanel.destroy();
                            } catch(Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            exitProgram(0);
                        }
                    }, null);
                }
            } catch(Exception e) {
                e.printStackTrace();
                exitProgram(0);
            }
        }               
        else exitProgram(0);
    }
    /**
     * Init pin related swings
     */
    private void pinInit() {
        pinPanelToggleButton = new ToggleButton("Pin panel");
        pinPanelToggleButton.setTheme("togglebutton");
        pinPanelToggleButton.setTooltipContent("Toggle pin panel options");
        pinPanelToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               boolean enabled = pinPanelToggleButton.isActive();
               menuOpened = enabled;
               setPinPanelButtonsVisible(enabled);
               setButtonsEnabled(! enabled);
               pinPanelToggleButton.setEnabled(true);
           }
        });
        add(pinPanelToggleButton);
        
        pinTypToggleButton = new ToggleButton("Pin note [text]");
        pinTypToggleButton.setTheme("togglebutton");
        pinTypToggleButton.setTooltipContent("Toggle pin types");
        pinTypToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               setPinTypeButtonsVisible(pinTypToggleButton.isActive());
           }
        });
        add(pinTypToggleButton);
        
        pinTxtToggleButton = new ToggleButton("Text");
        pinTxtToggleButton.setTheme("togglebutton");
        pinTxtToggleButton.setTooltipContent("Text notes.");
        pinTxtToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               pinNoteType = PinNote.TEXT_TYPE;
               pinTypToggleButton.setText("Pin note [text]");
               pinTypToggleButton.setActive(false);
               setPinTypeButtonsVisible(false);
               pinTxtToggleButton.setActive(true);
               pinImgToggleButton.setActive(false);
               pinAbsToggleButton.setActive(false);
           }
        });
        pinTxtToggleButton.setActive(true);
        add(pinTxtToggleButton);
        
        pinImgToggleButton = new ToggleButton("Image");
        pinImgToggleButton.setTheme("togglebutton");
        pinImgToggleButton.setTooltipContent("Image notes.");
        pinImgToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               pinNoteType = PinNote.IMAGE_TYPE;
               pinTypToggleButton.setText("Pin note [image]");
               pinTypToggleButton.setActive(false);
               setPinTypeButtonsVisible(false);
               pinTxtToggleButton.setActive(false);
               pinImgToggleButton.setActive(true);
               pinAbsToggleButton.setActive(false);
           }
        });
        add(pinImgToggleButton);
        
        pinAbsToggleButton = new ToggleButton("Absolute point");
        pinAbsToggleButton.setTheme("togglebutton");
        pinAbsToggleButton.setTooltipContent("Absolute points.");
        pinAbsToggleButton.addCallback(new Runnable(){
           @Override
        public void run(){
               pinNoteType = PinNote.ABSOLUTE_TYPE;
               pinTypToggleButton.setText("Pin note [abs.]");
               pinTypToggleButton.setActive(false);
               setPinTypeButtonsVisible(false);
               pinTxtToggleButton.setActive(false);
               pinImgToggleButton.setActive(false);
               pinAbsToggleButton.setActive(true);
           }
        });
        add(pinAbsToggleButton);
        
        openPinButton = new Button("Open ...");
        openPinButton.setTheme("button");
        openPinButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
        openPinButton.addCallback(new Runnable(){
           @Override
        public void run(){
               setPinPanelButtonsVisible(false);
               menuOpened = false;
               loadingPinPanel = true;
               openAPinPanelFile();
           }
        });
        add(openPinButton);
        
        newPinButton = new Button("New");
        newPinButton.setTheme("button");
        newPinButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
        newPinButton.addCallback(new Runnable(){
           @Override
        public void run(){
               newPinPanel();
               setPinPanelButtonsVisible(false);
               pinPanelToggleButton.setActive(false);
               setButtonsEnabled(true);
               menuOpened = false;
           }
        });
        add(newPinButton);
        
        savePinButton = new Button("Save");
        savePinButton.setTheme("button");
        savePinButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
        savePinButton.addCallback(new Runnable(){
           @Override
        public void run(){
               savePinPanel();
               setPinPanelButtonsVisible(false);
               pinPanelToggleButton.setActive(false);
               setButtonsEnabled(true);
               menuOpened = false;
           }
        });
        add(savePinButton);
        
        saveAsPinButton = new Button("Save as ...");
        saveAsPinButton.setTheme("button");
        saveAsPinButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
        saveAsPinButton.addCallback(new Runnable(){
           @Override
           public void run(){
               saveAsPinPanel();
               setPinPanelButtonsVisible(false);
               pinPanelToggleButton.setActive(false);
               setButtonsEnabled(true);
               menuOpened = false;
           }
        });
        //saveAsPinButton.setEnabled(false);
        add(saveAsPinButton);
        
        initPinButtonsEnabled();

    }

    protected void initPinButtonsEnabled() {
        // always enabled
        openPinButton.setEnabled(openedModel != null); 
        newPinButton.setEnabled(openedModel != null);
        savePinButton.setEnabled(pinPanel != null && pinPanel.hasChanges() && pinPanel.hasFileName());
        saveAsPinButton.setEnabled(pinPanel != null);
        pinTypToggleButton.setEnabled(openedModel != null && pinPanel != null && editMode);
    }
    protected void setPinPanelButtonsVisible(boolean visible) {
        openPinButton.setVisible(visible);
        savePinButton.setVisible(visible);
        saveAsPinButton.setVisible(visible);
        newPinButton.setVisible(visible);
    }
    
    protected void setPinTypeButtonsVisible(boolean visible) {
        pinTxtToggleButton.setVisible(visible);
        pinImgToggleButton.setVisible(visible);
        pinAbsToggleButton.setVisible(visible);
    }
    
    public void setButtonsEnabled(boolean enabled){
        open.setEnabled(enabled);
        pinPanelToggleButton.setEnabled(enabled);
        pinTypToggleButton.setEnabled(enabled && editMode);
        displayModesButton.setEnabled(enabled);
        help.setEnabled(enabled);
        credits.setEnabled(enabled);
        prtscr.setEnabled(enabled);
    }
    
    /**
     * @since 0.4
     * @version 0.4
     */
    public void openAFile(){ 
        fileSelector.setVisible(true);
        setButtonsEnabled(false);
    }
    

    /**
     * Opens a pin panel file
     */
    public void openAPinPanelFile(){ 
        fileSelector.setVisible(true);
        setButtonsEnabled(false);
        setPinPanelButtonsVisible(false);
        pinPanelToggleButton.setActive(false);
    }
    
    /**
     * Creates a new pin panel if possible.
     */
    protected void newPinPanel() {
        // TODO Auto-generated method stub
        Runnable callback = new Runnable() {
            public void run() {
                pinPanel = new PinPanel();
            }
        };
        if (pinPanel == null || pinPanel.hasChanges()) {
            if(defaultPath != null) {
                callback.run();
            }
            else {
                alertBox("Error", "No model opened.");
            }
        }
        else {
            confirmBox("New pin panel", "Are you sure? All unsaved changes will be lost.", callback, null);
        }

        initPinButtonsEnabled();
    }
    
    /**
     * Saves a new pin panel if possible.
     */
    protected void savePinPanel() {
        // TODO Auto-generated method stub
       if (pinPanel != null && pinPanel.hasChanges()) {
           try {
               pinPanel.save();
           } catch(Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
       }
       initPinButtonsEnabled();
    }
    
    /**
     * Creates a new pin panel if possible.
     */
    protected void saveAsPinPanel() {
        // TODO Auto-generated method stub
        if (pinPanel != null) {
            inputBox("Save as ... ", "Insert filename: ", modelName + "." + PinPanel.EXT, new Runnable() {
                public void run() {
                    String fname = msgBoxInput.getText();
                    if(fname.length() > 0) {
                        pinPanel.setFileLocation(defaultPath + fname);
                        try {
                            pinPanel.save();
                        } catch(Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }, null);
            initPinButtonsEnabled();
        }
    }
    
    /**
     * @since 0.4
     * @version 0.4
     */
    public void listDisplayModes(){ 
        okayVideoSetting.setVisible(true);
        cancelVideoSetting.setVisible(true);
        displayModeListBox.setVisible(true);
        fullscreenToggle.setVisible(true);
        setButtonsEnabled(false);
        displayModeListBox.setSelected(selectedResolution);
    }
    
    /**
     * @since 0.4
     * @version 0.4
     */
    public void confirmVideoSetting(){
        okayVideoSetting.setVisible(false);
        cancelVideoSetting.setVisible(false);
        displayModeListBox.setVisible(false);
        fullscreenToggle.setVisible(false);
        setButtonsEnabled(true);
        if(selectedResolution!=displayModeListBox.getSelected()){
            selectedResolution=displayModeListBox.getSelected();
            currentDisplayMode=displayModes[selectedResolution];
            try {
                Display.setDisplayMode(currentDisplayMode);
                settings.resWidth=currentDisplayMode.getWidth();
                settings.resHeight=currentDisplayMode.getHeight();
            } catch(LWJGLException e) {
                e.printStackTrace();
            }
            setupView();
            renderer.syncViewportSize();
            invalidateLayout();
        }
    }
    
    /**
     * @since 0.4
     * @version 0.4
     */
    public void cancelVideoSetting(){
        okayVideoSetting.setVisible(false);
        cancelVideoSetting.setVisible(false);
        displayModeListBox.setVisible(false);
        fullscreenToggle.setVisible(false);
        setButtonsEnabled(true);
    }
    
    /**
     * @since 0.4
     * @version 0.4
     */
    @Override
    protected void layout(){
        open.adjustSize();
        pinPanelToggleButton.adjustSize();
        openPinButton.adjustSize();
        newPinButton.adjustSize();
        savePinButton.adjustSize();
        saveAsPinButton.adjustSize();
        displayModesButton.adjustSize();
        stereoToggleButton.adjustSize();
        additionalContentToggleButton.adjustSize();
        /*
         * progressbar
         */
        progressBar.adjustSize();
        
        help.adjustSize();
        credits.adjustSize();
        prtscr.adjustSize();
        exit.adjustSize();
        int openHeight=Math.max(25,settings.resHeight/18);
        
        int buttonWidth=settings.resWidth/11+1;
        open.setSize(buttonWidth, openHeight);
        open.setPosition(0, 0);
        
        modeToggleButton.setSize(buttonWidth, openHeight);
        modeToggleButton.setPosition(buttonWidth, 0);
        
        additionalContentToggleButton.setSize(buttonWidth, openHeight);
        additionalContentToggleButton.setPosition(buttonWidth*2, 0);
        
        pinPanelToggleButton.setSize(buttonWidth, openHeight);
        pinPanelToggleButton.setPosition(buttonWidth*3, 0);
        
        newPinButton.setSize(buttonWidth, openHeight);
        newPinButton.setPosition(buttonWidth*3, openHeight);
        newPinButton.setVisible(false);
        
        openPinButton.setSize(buttonWidth, openHeight);
        openPinButton.setPosition(buttonWidth*3, openHeight*2);
        openPinButton.setVisible(false);
        
        savePinButton.setSize(buttonWidth, openHeight);
        savePinButton.setPosition(buttonWidth*3, openHeight*3);
        savePinButton.setVisible(false);
        
        saveAsPinButton.setSize(buttonWidth, openHeight);
        saveAsPinButton.setPosition(buttonWidth*3, openHeight*4);
        saveAsPinButton.setVisible(false);

        pinTypToggleButton.setSize(buttonWidth, openHeight);
        pinTypToggleButton.setPosition(buttonWidth*4, 0);
        
        pinTxtToggleButton.setSize(buttonWidth, openHeight);
        pinTxtToggleButton.setPosition(buttonWidth*4, openHeight);
        pinTxtToggleButton.setVisible(false);
        
        pinImgToggleButton.setSize(buttonWidth, openHeight);
        pinImgToggleButton.setPosition(buttonWidth*4, openHeight*2);
        pinImgToggleButton.setVisible(false);
        
        pinAbsToggleButton.setSize(buttonWidth, openHeight);
        pinAbsToggleButton.setPosition(buttonWidth*4, openHeight*3);
        pinAbsToggleButton.setVisible(false);
        
        displayModesButton.setPosition(buttonWidth*5, 0);
        displayModesButton.setSize(buttonWidth, openHeight);
        
        if(settings.stereoEnabled){
            stereoToggleButton.setPosition(buttonWidth*6, 0);
            stereoToggleButton.setSize(buttonWidth, openHeight/2);
            stereoScrollbar.setPosition(buttonWidth*6, openHeight/2);
            stereoScrollbar.setSize(buttonWidth, openHeight/2);
            //stereoScrollbar.setMinSize(settings.resWidth/36, openHeight);
        }else{
            stereoToggleButton.setPosition(buttonWidth*6, 0);
            stereoToggleButton.setSize(buttonWidth, openHeight);
            
            stereoScrollbar.setPosition(buttonWidth*6, openHeight);
            stereoScrollbar.setSize(buttonWidth, openHeight);
        }
        
        help.setPosition(buttonWidth*7, 0);
        help.setSize(buttonWidth, openHeight);
        credits.setPosition(buttonWidth*8, 0);
        credits.setSize(buttonWidth, openHeight);
        prtscr.setPosition(buttonWidth*9, 0);
        prtscr.setSize(buttonWidth, openHeight);
        exit.setPosition(buttonWidth*10, 0);
        exit.setSize(settings.resWidth-buttonWidth*10, openHeight);
        
        
        int rlWidth=settings.resWidth*8/10;
        int rlHeight=settings.resHeight*6/10;
        displayModeListBox.setSize(rlWidth,rlHeight);
        displayModeListBox.setPosition(settings.resWidth/2-rlWidth/2, settings.resHeight/6);
        
        fullscreenToggle.adjustSize();
        int fullToggleWidth=Math.max(fullscreenToggle.getWidth(),settings.resWidth/6);
        fullscreenToggle.setSize(fullToggleWidth, openHeight);
        fullscreenToggle.setPosition(settings.resWidth/2-rlWidth/2, settings.resHeight*19/24);
        
        cancelVideoSetting.adjustSize();
        int cancelVideoSettingWidth=Math.max(cancelVideoSetting.getWidth(),settings.resWidth/6);
        cancelVideoSetting.setSize(cancelVideoSettingWidth, openHeight);
        cancelVideoSetting.setPosition(settings.resWidth/2+rlWidth/2-cancelVideoSettingWidth, settings.resHeight*19/24);
        
        okayVideoSetting.adjustSize();
        int okayVideoSettingWidth=Math.max(okayVideoSetting.getWidth(),settings.resWidth/6);
        okayVideoSetting.setSize(okayVideoSettingWidth, openHeight);
        okayVideoSetting.setPosition(settings.resWidth/2+rlWidth/2-cancelVideoSettingWidth-okayVideoSettingWidth, settings.resHeight*19/24);
        
        fileSelector.adjustSize();
        int fsHeight=settings.resHeight*19/24+openHeight-settings.resWidth/2+rlWidth/2;
        fileSelector.setSize(rlWidth,fsHeight);
        fileSelector.setPosition(settings.resWidth/2-rlWidth/2, settings.resHeight/6);
        
        
        helpScrollPane.setSize(rlWidth, fsHeight);
        helpScrollPane.setPosition(settings.resWidth/2-rlWidth/2, settings.resHeight/6);
        helpTextArea.setSize(rlWidth, fsHeight);
        
        /*
         * Progressbar
         */
        progressBar.setSize(settings.resWidth, 30);
        progressBar.setPosition(0, settings.resHeight-30);
        
    }
    float inc = 0.001f;
    /**
     * @since 0.1
     * @version 0.4
     */
    private static void mainLoop(){
        init();
        setupView();
        fps=0;
        timePastFrame=(Sys.getTime()*1000)/Sys.getTimerResolution();
        timePastFps=timePastFrame;
        fpsToDisplay=0;
        
        boolean restart = false;
        
        while(!Display.isCloseRequested() && isRunning){
            resetView();
            //if (!inputTextMode) pollInput();
            pollInput();
            render();
            gui.update();
            Display.update();
            logic();
            Display.sync(settings.frequency);
            
            if(loadAsyncModel) {
                if(restart) {    
                    loadModel(defaultPath + modelName);
                    restart = false;
                    loadAsyncModel = false;
                }
                else {
                    restart = true;
                }
                
            }
        }
    }
    
    /**
     * @since 0.4
     * @version 0.4
     */
    private static void loadModel(String fileName){
        openedModel = null;
        System.gc();
        openedModel = new ObjOrPlyModel(fileName);
        System.gc();
        
        //Calculate the appropriate camera distance:
        //The following code takes the most extreme values on each coordinate
        //of all the specified vertices in the .obj file.
        //It uses the bigger distance (of two) from the average location on each axis
        //to calculate the radius of a circle that would surely enclose every vertex,
        //although allowing the radius to be slightly bigger than necessary.
        double d1=openedModel.minX-openedModel.centerx;
        double d2=openedModel.maxX-openedModel.centerx;
        double d3=openedModel.minY-openedModel.centery;
        double d4=openedModel.maxY-openedModel.centery;
        double d5=openedModel.minZ-openedModel.centerz;
        double d6=openedModel.maxZ-openedModel.centerz;
        d1*=d1;d2*=d2;d3*=d3;d4*=d4;d5*=d5;d6*=d6;
        d1=Math.max(d1, d2);
        d2=Math.max(d3, d4);
        d3=Math.max(d5, d6);
        d1=Math.sqrt(Math.max(Math.max(d1+d2, d2+d3), d1+d3));
        //
        double fovMin;//The smaller angle of view of the horizontal and vertical ones.
        if(settings.resWidth<settings.resHeight)fovMin=fovy*settings.resWidth/(double)settings.resHeight;else fovMin=fovy;
        fovMin=Math.PI*fovMin/180;//To radians.
        veinsRadius=d1/Math.sqrt(2);
        cameraZ=(float)(d1/Math.tan(fovMin/2));
        cameraX=0;cameraY=0;
        cameraOrientation=new Quaternion();
        
        double yAngle=Math.PI*fovy/360d;
        double xAngle=yAngle*settings.resWidth/settings.resHeight;
        double screenPlaneZ=-d1/Math.tan(fovMin/2);
        double screenPlaneY=Math.tan(yAngle)*(-screenPlaneZ);
        double screenPlaneX=Math.tan(xAngle)*(-screenPlaneZ);
        screenPlaneInitialUpperLeft=new double[]{-screenPlaneX, screenPlaneY, screenPlaneZ};
        screenPlaneInitialUpperRight=new double[]{screenPlaneX, screenPlaneY, screenPlaneZ};
        screenPlaneInitialLowerLeft=new double[]{-screenPlaneX, -screenPlaneY, screenPlaneZ};
        screenPlaneInitialLowerRight=new double[]{screenPlaneX, -screenPlaneY, screenPlaneZ};
        
        veinsGrabbedAt=null;
        double angle1= Math.PI*-90/180;
        double angle2= Math.PI*180/180;
        currentModelOrientation = Quaternion.quaternionFromAngleAndRotationAxis(angle1, new double[]{1,0,0});
        double[] v = Quaternion.quaternionReciprocal(currentModelOrientation).rotateVector3d(new double[]{0,1,0});
        currentModelOrientation=Quaternion.quaternionMultiplication(currentModelOrientation, Quaternion.quaternionFromAngleAndRotationAxis(angle2, v));
        addedModelOrientation = new Quaternion();
        
        
    }
    private static void loadPinPanel(String filePath) {
        try {
            pinPanel = PinPanel.open(filePath);
        } catch(IOException e) {
            // TODO error message if pin could not be opened
            e.printStackTrace();
        }
    }
    
    /**
     * @since 0.1
     * @version 0.4
     */
    private static void init(){
        //load textures
        try {
            rotationCircle = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/rotationCircle.png"));
        } catch(IOException e) {
            System.err.println("Loading texture main/rotationCircle.png unsuccessful");
            e.printStackTrace();
        }
        try {
            circleGlow = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/circleGlow.png"));
        } catch(IOException e) {
            System.err.println("Loading texture main/circleGlow.png unsuccessful");
            e.printStackTrace();
        }
        try {
            movementCircle = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/movementCircle.png"));
        } catch(IOException e) {
            System.err.println("Loading texture main/movementCircle.png unsuccessful");
            e.printStackTrace();
        }
        try {
            rotationElipse = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/rotationElipse.png"));
        } catch(IOException e) {
            System.err.println("Loading texture main/rotationElipse.png unsuccessful");
            e.printStackTrace();
        }
        try {
            movementElipse = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/movementElipse.png"));
        } catch(IOException e) {
            System.err.println("Loading texture main/movementElipse.png unsuccessful");
            e.printStackTrace();
        }
        try {
            ellipseGlow = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/ellipseGlow.png"));
        } catch(IOException e) {
            System.err.println("Loading texture main/ellipseGlow.png unsuccessful");
            e.printStackTrace();
        }
        try {
            bubbleTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("main/bubble.png"));
            Bubbles.setTexture(bubbleTexture);
        } catch(IOException e) {
            System.err.println("Loading texture main/bubble.png unsuccessful");
            e.printStackTrace();
        }
    }
    
    /**
     * @since 0.1
     * @version 0.1
     */
    private static void drawHUD(){
        
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        //prepare
        glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, settings.resWidth, 0, settings.resHeight, 0.2f, 20);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glClearColor(0f, 0f, 0f, 0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        //begin drawing
        float w=settings.resWidth;
        float h=settings.resHeight;
        float r=w/18;
        float offset=r*2/3;
        float x=w-offset-r;
        float y=h-h/18-offset-r;
        float x2=w-offset-r;
        float y2=h-h/18-2*offset-3*r;
        
        glColor4f(1,1,1,1);
        GL11.glBindTexture(GL_TEXTURE_2D, rotationElipse.getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex3f(x+1.5f*r, y+r, -1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(x-1.5f*r, y+r, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(x-1.5f*r, y-r, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(x+1.5f*r, y-r, -1.0f);
        glEnd();
        
        
        
        GL11.glBindTexture(GL_TEXTURE_2D, movementElipse.getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex3f(x2+1.5f*r, y2+r, -1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(x2-1.5f*r, y2+r, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(x2-1.5f*r, y2-r, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(x2+1.5f*r, y2-r, -1.0f);
        glEnd();
        
        
        
        if(clickedOn==CLICKED_ON_MOVE_ELLIPSE || clickedOn==CLICKED_ON_ROTATION_ELLIPSE){
            float x3=x, y3=y;
            if(clickedOn==CLICKED_ON_MOVE_ELLIPSE){x3=x2; y3=y2;}
            glPushMatrix();
            glTranslatef(x3, y3, 0);
            if(ellipseSide==0)glRotatef((180), 0, 0, 1);
            glTranslatef(-x3, -y3, 0);
            GL11.glBindTexture(GL_TEXTURE_2D, ellipseGlow.getTextureID());
            glColor4f(1,1,1, 0.5f);
            glBegin(GL_QUADS);
            glTexCoord2f(1, 0);
            glVertex3f(x3+1.5f*r, y3+r, -0.8f);
            glTexCoord2f(0, 0);
            glVertex3f(x3-1.5f*r, y3+r, -0.8f);
            glTexCoord2f(0, 1);
            glVertex3f(x3-1.5f*r, y3-r, -0.8f);
            glTexCoord2f(1, 1);
            glVertex3f(x3+1.5f*r, y3-r, -0.8f);
            glEnd();
            glPopMatrix();
        }
        glColor4f(1,1,1,1);
        GL11.glBindTexture(GL_TEXTURE_2D, rotationCircle.getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex3f(x+r, y+r, -0.6f);
        glTexCoord2f(0, 0);
        glVertex3f(x-r, y+r, -0.6f);
        glTexCoord2f(0, 1);
        glVertex3f(x-r, y-r, -0.6f);
        glTexCoord2f(1, 1);
        glVertex3f(x+r, y-r, -0.6f);
        glEnd();
        
        
      //begin drawing
        GL11.glBindTexture(GL_TEXTURE_2D, movementCircle.getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex3f(x2+r, y2+r, -0.6f);
        glTexCoord2f(0, 0);
        glVertex3f(x2-r, y2+r, -0.6f);
        glTexCoord2f(0, 1);
        glVertex3f(x2-r, y2-r, -0.6f);
        glTexCoord2f(1, 1);
        glVertex3f(x2+r, y2-r, -0.6f);
        glEnd();
        if(clickedOn==CLICKED_ON_ROTATION_CIRCLE || clickedOn==CLICKED_ON_MOVE_CIRCLE){
            if(clickedOn==CLICKED_ON_MOVE_CIRCLE){x=x2; y=y2;}
            glPushMatrix();
            glTranslatef(x, y, 0);
            glRotatef((float)(180*rotationCircleAngle/Math.PI), 0, 0, 1);
            glTranslatef(-x, -y, 0);
            GL11.glBindTexture(GL_TEXTURE_2D, circleGlow.getTextureID());
            glColor4f(1,1,1,rotationCircleDistance);
            glBegin(GL_QUADS);
            glTexCoord2f(1, 0);
            glVertex3f(x+r, y+r, -0.4f);
            glTexCoord2f(0, 0);
            glVertex3f(x-r, y+r, -0.4f);
            glTexCoord2f(0, 1);
            glVertex3f(x-r, y-r, -0.4f);
            glTexCoord2f(1, 1);
            glVertex3f(x+r, y-r, -0.4f);
            glEnd();
            glPopMatrix();
        }
        
        //exit "HUD" mode
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LIGHTING);
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
    }
    
    /**
     * @since 0.1
     * @version 0.2
     */
    private static void setupView(){
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, settings.resWidth, settings.resHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(fovy, settings.resWidth/(float)settings.resHeight, zNear, zFar);
        glShadeModel(GL_SMOOTH);
        setCameraAndLight(0);
    }
    
    /**
     * @since 0.1
     * @version 0.1
     */
    private static void resetView(){
        glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    /**
     * @since 0.1
     * @version 0.1
     */
    private static void setCameraAndLight(float offset){
        double v[]= new double[]{offset, 0, 0};
        v=cameraOrientation.rotateVector3d(v);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        Quaternion worldOrientation = Quaternion.quaternionReciprocal(cameraOrientation);
        glMultMatrix(worldOrientation.getRotationMatrix(false));
        glTranslatef(-cameraX+(float)v[0], -cameraY+(float)v[1], -cameraZ+(float)v[2]); 
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLight(GL_LIGHT0,GL_POSITION,allocFloats(lightOrigin)); // Set light source
        glLight(GL_LIGHT0,GL_DIFFUSE,allocFloats(new float[]{1f,1f,1f,1}));
        glLight(GL_LIGHT0,GL_AMBIENT,allocFloats(new float[]{0.3f,0.3f,0.3f,1}));
        glLight(GL_LIGHT0,GL_SPECULAR,allocFloats(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
    }
    
    public static void initStencil(){
        //Stencil
        glStencilMask(0x01);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        //disable writing colors and depth
        glColorMask(false, false, false, false);
        glDepthMask(false);
        //Make the stencil test always fail and on fail replace the appropriate value in stencil buffer to 1
        glStencilFunc(GL11.GL_NEVER, 1, 0x01);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        glStencilMask(0x01);//enable writing to bitplanes
        //prepare ortho drawing
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, settings.resWidth, 0, settings.resHeight, 0.2f, 3);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        //draw the lines
        glTranslatef(0.375f,0.375f,-2);
        glColor4f(1,1,1,1);
        for(int i=0;i<settings.resHeight;i+=2){
            glLineWidth(1);
            glBegin(GL_LINES);
            glVertex2f(0,i);
            glVertex2f(settings.resWidth,i);
            glEnd();
        }
        //exit ortho drawing
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
        //enable writing colors and depth and disable writing to bitplanes
        glColorMask(true, true, true, true);
        glDepthMask(true);
        glStencilMask(0x00);
    }
    
    private static void renderVeins(){
        if(openedModel!=null){
            glMatrixMode(GL_MODELVIEW);
            glPushMatrix();
            Quaternion compositeOrientation=Quaternion.quaternionMultiplication(currentModelOrientation, addedModelOrientation);
            FloatBuffer fb= compositeOrientation.getRotationMatrix(false);
            GL11.glMultMatrix(fb);
            
            glEnable(GL_LIGHTING);
            glColor4f(0.8f, 0.06667f, 0.0f, 1);
            glMaterial(GL_FRONT,GL_AMBIENT, allocFloats(new float[]{0.8f, 0.06667f, 0.0f, 1}));
            glMaterial(GL_FRONT,GL_DIFFUSE, allocFloats(new float[]{0.8f, 0.06667f, 0.0f, 1}));
            glMaterial(GL_FRONT,GL_SPECULAR, allocFloats(new float[]{0.0f, 0.0f, 0.0f, 1f}));
            glMaterial(GL_FRONT, GL_SHININESS, allocFloats(new float[]{0.5f, 0.25f, 0.25f, 0.25f}));
            openedModel.render(shaderPrograms[activeShaderProgram-1]);
            GL20.glUseProgram(0);
            glPopMatrix();
        }
    }
    
    /**
     * @since 0.1
     * @version 0.4
     */
    private static void render(){
        if(settings.stereoEnabled){
            float offset=settings.stereoValue/10f;
            initStencil();
            glStencilFunc(GL_EQUAL, 0, 0x01);
            setCameraAndLight(offset);
            renderVeins();
            glStencilFunc(GL_EQUAL, 1, 0x01);
            setCameraAndLight(-offset);
            renderVeins();
            glDisable(GL_STENCIL_TEST);
        }
        else{
            setCameraAndLight(0);
            renderVeins();
            
          //TODO move to if pins visible
            if(pinPanel != null){
                if(pinPanel.pointsChanged) {
                    for(PinNote nt : pinPanel.getNotes()) {
                        if(nt.getType() == PinNote.ABSOLUTE_TYPE) {
                            bubbles_absolutePoints.add(new float[] {
                                    (float)nt.getX(), 
                                    (float)nt.getY(), 
                                    (float)nt.getZ()
                            });
                        } else if (nt.getType() == PinNote.IMAGE_TYPE) {
                            bubbles_absolutePoints.add(new float[] {
                                    (float)nt.getX(), 
                                    (float)nt.getY(), 
                                    (float)nt.getZ()
                            });
                        } else if (nt.getType() == PinNote.TEXT_TYPE) {
                            bubbles_absolutePoints.add(new float[] {
                                    (float)nt.getX(), 
                                    (float)nt.getY(), 
                                    (float)nt.getZ()
                            });
                        }
                    }
                    pinPanel.pointsChanged = false;
                }
            }
            else {
                // Clear arrays
                bubbles_absolutePoints = new ArrayList<float[]>();
                bubbles_images = new ArrayList<float[]>();
                bubbles_text = new ArrayList<float[]>();
            }
            
            ////////////////////// XXXXXX
            if (pinsVisible) {
                if(bubbles_absolutePoints.size()>0){
                    for(float[] bubble:bubbles_absolutePoints){
                        Bubbles.drawBubble(bubble[0], bubble[1], bubble[2], "red");
                    }
                }
                if(bubbles_images.size()>0){
                    for(float[] bubble:bubbles_images){
                        Bubbles.drawBubble(bubble[0], bubble[1], bubble[2], "green");
                    }
                }
                if(bubbles_text.size()>0){
                    for(float[] bubble:bubbles_text){
                        Bubbles.drawBubble(bubble[0], bubble[1], bubble[2], "blue");
                    }
                }
            }
            ///////////////////// XXXXXX
            
        }
        //HUD
        drawHUD();
        if(settings.isFpsShown)Display.setTitle(title+" - FPS: "+fpsToDisplay);else Display.setTitle(title);
    }
    
    private static double[] getRaySphereIntersection(int x, int y){
        //figure out if the click on the screen intersects the circle that surrounds the veins model
        double[] d=getRayDirection(x, y);//get the direction of the "ray" cast from the camera location
        double[] e=new double[]{cameraX, cameraY, cameraZ};//a vector representing the camera location
        double[] c=new double[3];//the location of the sphere is the zero vector
        //partial calculations
        double[] eSc=Vector.subtraction(e, c);
        double dDPeSc=Vector.dotProduct(d, eSc);
        double discriminant = dDPeSc*dDPeSc-Vector.dotProduct(d, d)*(Vector.dotProduct(eSc, eSc)-veinsRadius*veinsRadius);
        if(discriminant<0){//in this case the mouse is not pressed near the veins sphere
            return null;
        }else{// in this case we hold the mouse on the sphere surrounding the veins model in some way
            double [] Sd=Vector.subtraction(new double[3], d);//partial calculation
            //t1 and t2 are the parameter values for vor "ray" expression e+t*d
            double t1=(Vector.dotProduct(Sd, eSc)+Math.sqrt(discriminant))/Vector.dotProduct(d, d);
            double t2=(Vector.dotProduct(Sd, eSc)-Math.sqrt(discriminant))/Vector.dotProduct(d, d);
            
            if(t2<0)return Vector.sum(e, Vector.vScale(d, t1));else return Vector.sum(e, Vector.vScale(d, t2));
        }
    }
    private static double[] getRayDirection(int x, int y){
        double[] tempUpperLeft=cameraOrientation.rotateVector3d(screenPlaneInitialUpperLeft);
        double[] tempLowerLeft=cameraOrientation.rotateVector3d(screenPlaneInitialLowerLeft);
        double[] tempLowerRight=cameraOrientation.rotateVector3d(screenPlaneInitialLowerRight);
        
        double[] leftToRight=Vector.subtraction(tempLowerRight, tempLowerLeft);
        leftToRight=Vector.vScale(leftToRight, (0.5d+x)/settings.resWidth);
        double[]rayD=Vector.sum(tempLowerLeft,leftToRight);
        
        double[] downToUp=Vector.subtraction(tempUpperLeft, tempLowerLeft);
        downToUp=Vector.vScale(downToUp, (0.5d+y)/settings.resHeight);
        
        rayD=Vector.sum(rayD,downToUp);
        
        return rayD;
    }
    
    private static boolean isAAEnabled=false, wireframe=false;

    private static boolean ctrlPressed = false;
    private static boolean calcPoint = false;

    static int flagRotate = 0; // flag for rotating plain
    static int flagLowerUpperPlain = 0; //flag for moving plain up and down

    private static int dragDialogX = -1;
    private static int dragDialogY = -1;
    
    /**
     * @since 0.1
     * @version 0.4
     */
    private static void pollInput(){
       /* if(inputTextMode) {

           return;
        }*/
        int z=Mouse.getDWheel();
        
        if(pinItrPane!=null && imageWidget != null && z != 0) {
            if(z>0) {
                imageWidget.scrollImage(1.1);
                pinItrPane.updateScrollbarSizes();
            } else {
                imageWidget.scrollImage(0.9);
                pinItrPane.updateScrollbarSizes();
            }
        }
        
        if (dialogOpened && draggableWidget != null && Mouse.isButtonDown(LMB)) {
            int x = Mouse.getX();
            int y = settings.resHeight - Mouse.getY();

            if (dragDialogX < 0 || dragDialogY < 0) {
                int lx = draggableWidget.getX();
                int ty = draggableWidget.getY();
                int rx = draggableWidget.getWidth() + lx;
                int by = draggableWidget.getHeight() + ty;

                if (x >= lx && x <= rx && y >= ty && y <= by) {
                    dragDialogX = x;
                    dragDialogY = y; 
                }
            }
            else {
                int dirX = (x - dragDialogX);
                int dirY = (y - dragDialogY);
                
                dragDialogX = x;
                dragDialogY = y;
                gameUI.moveDialogs(dirX, dirY);
            }
            
        } else if(dialogOpened && draggableWidget != null && !Mouse.isButtonDown(LMB)) {
            dragDialogX = -1;
            dragDialogY = -1;   
        }
        
        // Go out if dialog Opened of menu opened
        if(dialogOpened || menuOpened ) return;
        
    
        if(! calcPoint && Mouse.hasWheel() && Mouse.isButtonDown(MWB) || ctrlPressed) {
            int evnt = Mouse.getEventButton();
            if(evnt == MWB || ctrlPressed && evnt == LMB) {
                // mouse wheel clicked
                calcPoint = true;
                float[] pointOnModelClickedUpon = getClickedPointOnLoadedModel();
                if(pointOnModelClickedUpon!=null){
                    lastRay = pointOnModelClickedUpon;
                    
                    //bubbles_absolutePoints.add(pointOnModelClickedUpon); 
                    //bubbles_images.add(pointOnModelClickedUpon);
                    //bubbles_text.add(pointOnModelClickedUpon);
                }
                
                
                if(editMode) gameUI.editPinNote();
                else gameUI.showPinNote();
                ctrlPressed = false;
                calcPoint = false;
            }
        }
        
        
        if(Keyboard.getEventKey() == Keyboard.KEY_I) {if(Keyboard.getEventKeyState()){flagRotate = 1;}else{flagRotate = 0;}}
        if(Keyboard.getEventKey() == Keyboard.KEY_K) {if(Keyboard.getEventKeyState()){flagRotate = -1;}else{flagRotate = 0;}}
        if(Keyboard.getEventKey() == Keyboard.KEY_O) {if(Keyboard.getEventKeyState()){flagLowerUpperPlain = 1;}else{flagLowerUpperPlain = 0;}}
        if(Keyboard.getEventKey() == Keyboard.KEY_L) {if(Keyboard.getEventKeyState()){flagLowerUpperPlain = -1;}else{flagLowerUpperPlain = 0;}}
        
        if(flagRotate!=0) {openedModel.rotatePlain(0, flagRotate*40);}
        if(flagLowerUpperPlain!=0) {openedModel.incPlain(0, (float)(flagLowerUpperPlain*0.2));}
           
        
        
        
        // Go out when in inputTextMode
        if(inputTextMode) {
            return;
        }

        
        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()){//if a key was pressed (vs. released)
                if(Keyboard.getEventKey()==Keyboard.KEY_TAB){
                    if(settings.isFpsShown)settings.isFpsShown=false;else settings.isFpsShown=true;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_P) {
                    openedModel.changePlainState();    
                }else if(Keyboard.getEventKey()==Keyboard.KEY_Z) {
                    generatePlaneIntersectionImage(openedModel.planeIntersection());
                }else if(Keyboard.getEventKey()==Keyboard.KEY_O) {
                    openedModel.incPlain(0, (float)0.2);
                }else if(Keyboard.getEventKey()==Keyboard.KEY_L) {
                    openedModel.incPlain(0, (float)-0.2);
                }else if(Keyboard.getEventKey()==Keyboard.KEY_I) {
                    openedModel.rotatePlain(0, 20);
                }else if(Keyboard.getEventKey()==Keyboard.KEY_K) {
                    openedModel.rotatePlain(0, -20);                
                    
                    
                    
                }else if(Keyboard.getEventKey()==Keyboard.KEY_H) { // Next 4 are for changing light origin
                    //lightOrigin = new float[]{1000.0f, 0.0f, 0.0f , 0.0f};
                    if (lightOriginAngleY > -180) {
                        lightOriginAngleY -= 10;
                        lightOrigin[1] = (float) (lightDistance * Math.cos(Math.toRadians(lightOriginAngleY)));
                        lightOrigin[2] = (float) (lightDistance * Math.sin(Math.toRadians(lightOriginAngleY)));
                    }
                }else if(Keyboard.getEventKey()==Keyboard.KEY_N) {                    
                    if (lightOriginAngleY < 0) {
                        lightOriginAngleY += 10;
                        lightOrigin[1] = (float) (lightDistance * Math.cos(Math.toRadians(lightOriginAngleY)));
                        lightOrigin[2] = (float) (lightDistance * Math.sin(Math.toRadians(lightOriginAngleY)));
                    }
                }else if(Keyboard.getEventKey()==Keyboard.KEY_B) {
                    if (lightOriginAngleX < 0) {
                        lightOriginAngleX += 10;
                        lightOrigin[0] = (float) (lightDistance * Math.cos(Math.toRadians(lightOriginAngleX)));
                        lightOrigin[2] = (float) (lightDistance * Math.sin(Math.toRadians(lightOriginAngleX)));
                    }
                }else if(Keyboard.getEventKey()==Keyboard.KEY_M) {                    
                    if (lightOriginAngleX > -180) {
                        lightOriginAngleX -= 10;
                        lightOrigin[0] = (float) (lightDistance * Math.cos(Math.toRadians(lightOriginAngleX)));
                        lightOrigin[2] = (float) (lightDistance * Math.sin(Math.toRadians(lightOriginAngleX)));
                    }
                    
                    

                }else if(Keyboard.getEventKey()==Keyboard.KEY_LCONTROL) {
                    ctrlPressed = true;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_RCONTROL) {
                    ctrlPressed = true;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_LMETA) {
                    ctrlPressed = true;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_RMETA) {
                    ctrlPressed = true;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_1){
                    activeShaderProgram=1;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_2){
                    activeShaderProgram=2;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_3){
                    activeShaderProgram=3;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_4){
                    activeShaderProgram=4;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_5){
                    activeShaderProgram=5;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_6){
                    activeShaderProgram=6;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_7){
                    activeShaderProgram=7;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_8){
                    activeShaderProgram=8;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_9){
                    activeShaderProgram=9;
                }else if(Keyboard.getEventKey()==Keyboard.KEY_0){
                    if(!(wireframe=!wireframe))GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
                    else GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
                }else if(Keyboard.getEventKey()==Keyboard.KEY_ADD){
                    openedModel.increaseSubdivisionDepth();
                }else if(Keyboard.getEventKey()==Keyboard.KEY_SUBTRACT){
                    openedModel.decreaseSubdivisionDepth();
                }else if(Keyboard.getEventKey()==Keyboard.KEY_9){
                    isAAEnabled=!isAAEnabled;
                }else {
                    ctrlPressed = false;
                }
            }
            else {
                ctrlPressed = false;
            }
        }
        


        // Model pan, zoom and rotation
//      //int z=Mouse.getDWheel();

        if(z>0){
            cameraX*=0.8;
            cameraY*=0.8;
            cameraZ*=0.8;
            zoom*=1/0.8;
        }else if(z<0){
            cameraX*=1.25;
            cameraY*=1.25;
            cameraZ*=1.25;
            zoom*=1/1.25;
        }
        //moving the camera
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
          //create a vector representing the rotation axis
            Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{1,0,0});
            cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{-1,0,0});
            cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{0,1,0});
            cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{0,-1,0});
            cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{0,0,1});
            cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{0,0,-1});
            cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            double v[]= new double[]{0, 0, -1};
            v=cameraOrientation.rotateVector3d(v);
            cameraX+=(float)v[0];
            cameraY+=(float)v[1];
            cameraZ+=(float)v[2];
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            double v[]= new double[]{0, 0, 1};
            v=cameraOrientation.rotateVector3d(v);
            cameraX+=(float)v[0];
            cameraY+=(float)v[1];
            cameraZ+=(float)v[2];
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            double v[]= new double[]{1, 0, 0};
            v=cameraOrientation.rotateVector3d(v);
            cameraX+=(float)v[0];
            cameraY+=(float)v[1];
            cameraZ+=(float)v[2];
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            double v[]= new double[]{-1, 0, 0};
            v=cameraOrientation.rotateVector3d(v);
            cameraX+=(float)v[0];
            cameraY+=(float)v[1];
            cameraZ+=(float)v[2];
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R)){
            double v[]= new double[]{0, 1, 0};
            v=cameraOrientation.rotateVector3d(v);
            cameraX+=(float)v[0];
            cameraY+=(float)v[1];
            cameraZ+=(float)v[2];
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_F)){
            double v[]= new double[]{0, -1, 0};
            v=cameraOrientation.rotateVector3d(v);
            cameraX+=(float)v[0];
            cameraY+=(float)v[1];
            cameraZ+=(float)v[2];
        }
        
        if(openedModel!=null){
            if(Mouse.isButtonDown(LMB)){
                //figure out if clicked on the HUD first
                float w=settings.resWidth;
                float h=settings.resHeight;
                float r=w/18;
                float offset=r*2/3;
                float x=w-offset-r;
                float y=h-h/18-offset-r;
                float x2=w-offset-r;
                float y2=h-h/18-2*offset-3*r;
                float f=ellipsef*r;
                
                if(clickedOn==CLICKED_ON_NOTHING){
                    float distanceToRotationCircle=(x-Mouse.getX())*(x-Mouse.getX())+(y-Mouse.getY())*(y-Mouse.getY());
                    float distanceToMoveCircle=(x2-Mouse.getX())*(x2-Mouse.getX())+(y2-Mouse.getY())*(y2-Mouse.getY());
                    float distanceToRotationFoci=(float)(Math.sqrt((x-f-Mouse.getX())*(x-f-Mouse.getX())+(y-Mouse.getY())*(y-Mouse.getY()))+
                            Math.sqrt((x+f-Mouse.getX())*(x+f-Mouse.getX())+(y-Mouse.getY())*(y-Mouse.getY())));
                    float distanceToMoveFoci=(float)(Math.sqrt((x2-f-Mouse.getX())*(x2-f-Mouse.getX())+(y2-Mouse.getY())*(y2-Mouse.getY()))+
                            Math.sqrt((x2+f-Mouse.getX())*(x2+f-Mouse.getX())+(y2-Mouse.getY())*(y2-Mouse.getY())));
                    if(settings.resHeight-Mouse.getY()<settings.resHeight/18){
                        clickedOn=CLICKED_ON_BUTTONS;
                    }else if(distanceToRotationCircle<=r*r){
                        clickedOn=CLICKED_ON_ROTATION_CIRCLE;
                    }else if(distanceToMoveCircle<=r*r){
                        clickedOn=CLICKED_ON_MOVE_CIRCLE;
                    }else if(distanceToRotationFoci<=r*3f){
                        clickedOn=CLICKED_ON_ROTATION_ELLIPSE;
                    }else if(distanceToMoveFoci<=r*3f){
                        clickedOn=CLICKED_ON_MOVE_ELLIPSE;
                    }else {
                        /*
                        float[] pointOnModelClickedUpon = getClickedPointOnLoadedModel();
                        if(pointOnModelClickedUpon!=null){
                            lastRay = pointOnModelClickedUpon;
                            newLastRay = true;
                            //bubbles_absolutePoints.add(pointOnModelClickedUpon); 
                            //bubbles_images.add(pointOnModelClickedUpon);
                            //bubbles_text.add(pointOnModelClickedUpon);
                        }
                        */
                        
                        veinsGrabbedAt=getRaySphereIntersection(Mouse.getX(), Mouse.getY());
                        addedModelOrientation=new Quaternion();
                        if(veinsGrabbedAt!=null)clickedOn=CLICKED_ON_VEINS_MODEL;
                    }
                }
                
                if(clickedOn==CLICKED_ON_VEINS_MODEL){
                    double[] veinsHeldAt=getRaySphereIntersection(Mouse.getX(), Mouse.getY());
                    if(veinsHeldAt!=null){
                        double[] rotationAxis=Vector.crossProduct(veinsGrabbedAt, veinsHeldAt);
                        if(Vector.length(rotationAxis)>0){
                            rotationAxis=Vector.normalize(rotationAxis);
                            rotationAxis=Quaternion.quaternionReciprocal(currentModelOrientation).rotateVector3d(rotationAxis);
                            double angle=Math.acos(Vector.dotProduct(veinsGrabbedAt, veinsHeldAt)/(Vector.length(veinsGrabbedAt)*Vector.length(veinsHeldAt)));
                            addedModelOrientation= Quaternion.quaternionFromAngleAndRotationAxis(angle, rotationAxis);
                        }
                    }
                }
                if(clickedOn==CLICKED_ON_ROTATION_CIRCLE || clickedOn==CLICKED_ON_MOVE_CIRCLE){
                    if(clickedOn==CLICKED_ON_MOVE_CIRCLE){x=x2;y=y2;}
                    rotationCircleDistance=(x-Mouse.getX())*(x-Mouse.getX())+(y-Mouse.getY())*(y-Mouse.getY());
                    rotationCircleAngle=(float)Math.atan2(Mouse.getY()-y, Mouse.getX()-x);
                    rotationCircleDistance=(float)Math.sqrt(rotationCircleDistance);
                    float upRotation=(Mouse.getY()-y);
                    float rightRotation=(Mouse.getX()-x);
                    if(rotationCircleDistance>r){
                        upRotation/=rotationCircleDistance;
                        rightRotation/=rotationCircleDistance;
                    }else{
                        upRotation/=r;
                        rightRotation/=r;
                    }
                    rotationCircleDistance=Math.min(rotationCircleDistance, r)/r;
                    if(clickedOn==CLICKED_ON_ROTATION_CIRCLE){
                        Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed*upRotation, new double[]{1,0,0});
                        cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
                        addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed*rightRotation, new double[]{0,-1,0});
                        cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
                    }else{
                        double up[]= new double[]{0, 0, -upRotation};
                        up=cameraOrientation.rotateVector3d(up);
                        double right[]= new double[]{rightRotation, 0, 0};
                        right=cameraOrientation.rotateVector3d(right);
                        cameraX+=(float)(up[0]+right[0]);
                        cameraY+=(float)(up[1]+right[1]);
                        cameraZ+=(float)(up[2]+right[2]);
                    }
                }
                if(clickedOn==CLICKED_ON_ROTATION_ELLIPSE){
                    if(x-Mouse.getX()<=0){
                        ellipseSide=0;
                        Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{0,0,-1});
                        cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
                    }else{
                        ellipseSide=1;
                        Quaternion addRotation = Quaternion.quaternionFromAngleAndRotationAxis(cameraRotationSpeed, new double[]{0,0,1});
                        cameraOrientation=Quaternion.quaternionMultiplication(cameraOrientation, addRotation);
                    }
                }
                if(clickedOn==CLICKED_ON_MOVE_ELLIPSE){
                    double v[];
                    if(x2-Mouse.getX()<=0){
                        ellipseSide=0;
                        v= new double[]{0, 1, 0};
                    }else{
                        ellipseSide=1;
                        v= new double[]{0, -1, 0};
                    }
                    v=cameraOrientation.rotateVector3d(v);
                    cameraX+=(float)v[0];
                    cameraY+=(float)v[1];
                    cameraZ+=(float)v[2];
                }
                
                
            }else if(Mouse.isButtonDown(1)){ // When user clicks and moves right mouse button
                
                int mouse_diff_x = Mouse.getX() - mouse_x;
                int mouse_diff_y = Mouse.getY() - mouse_y;
                double model_size_ratio = veinsRadius/425;
                
                // Moving mouse left - move object left - camera right (and vice versa)             
                // Moving mouse up   - move object up -   camera down (and vice versa)
                if (mouse_diff_x != 0 || mouse_diff_y != 0) {
                    double v[]= new double[]{-mouse_diff_x * model_size_ratio / zoom, -mouse_diff_y * model_size_ratio / zoom, 0};
                    v=cameraOrientation.rotateVector3d(v);
                    cameraX+=(float)v[0];
                    cameraY+=(float)v[1];
                    cameraZ+=(float)v[2];   
                }
   
            }else{
                clickedOn=CLICKED_ON_NOTHING;
                veinsGrabbedAt=null;
                currentModelOrientation=Quaternion.quaternionMultiplication(currentModelOrientation, addedModelOrientation);
                addedModelOrientation=new Quaternion();
            }
            
            // Remember last mouse position
            mouse_x = Mouse.getX();
            mouse_y = Mouse.getY();
        }
    }
    
    public static float[] getClickedPointOnLoadedModel(){
        Quaternion compositeOrientation=Quaternion.quaternionMultiplication(currentModelOrientation, addedModelOrientation);
        Quaternion q = Quaternion.quaternionReciprocal(compositeOrientation);
        double[] d = new double[]{0,0,-1};
        double angleY = Math.atan(   ((2*Mouse.getY()-settings.resHeight)/(double)settings.resHeight)*Math.tan(fovy*Math.PI/360d)  );
        double fovx = Math.atan(2*((double)settings.resWidth/(double)settings.resHeight) * Math.tan(fovy*Math.PI/360d));
        double angleX = Math.atan(   ((2*Mouse.getX()-settings.resWidth)/(double)settings.resWidth)*Math.tan(fovx)/2  );
        double[] d1=Quaternion.quaternionFromAngleAndRotationAxis(angleY, new double[]{1,0,0}).rotateVector3d(d);
        double[] d2=Quaternion.quaternionFromAngleAndRotationAxis(angleX, new double[]{0,-1,0}).rotateVector3d(d);
        d=Vector.subtraction(Vector.sum(Vector.vScale(d1, Math.abs(1/d1[2])), Vector.vScale(d2, Math.abs(1/d2[2]))), new double[]{0,0,-1});
        d=cameraOrientation.rotateVector3d(d);
        double[] e=new double[]{cameraX, cameraY, cameraZ};
        d=q.rotateVector3d(d);
        e=q.rotateVector3d(e);
        double[] c=new double[]{-(float)openedModel.centerx,-(float)openedModel.centery,-(float)openedModel.centerz};
        double[] eSc=Vector.subtraction(e, c);
        return openedModel.rtreeOfTriangles_forPlyFiles.findAllIntersectedPoints(new float[]{(float)eSc[0], (float)eSc[1], (float)eSc[2]}, new float[]{(float)d[0], (float)d[1], (float)d[2]});
    }
    
    private static void generatePlaneIntersectionImage(LinkedList<float[]> planeIntersection) {
        // TODO Auto-generated method stub
        
    }
    private void editPinNote() {
        if(pinPanel == null) return;
        note = pinPanel.getNearest(lastRay[0], lastRay[1], lastRay[2]); // TODO: get nearest
        System.out.println(lastRay[0] + " " + lastRay[1] + " " + lastRay[2]);
        if (note != null && note.distanceTo(lastRay[0], lastRay[1], lastRay[2]) > MAX_RAY_DISTANCE) {
            note = null;
        }
        dialogOpened = true; 
        inputTextMode = true;

        int cw = settings.resWidth/2;
        int ch = settings.resHeight/2;
        // TODO: determine if note OK
        EditFieldModel efm;
        SimpleTextAreaModel stmTit;
        switch (note == null ? pinNoteType : note.getType()) {
            case PinNote.ABSOLUTE_TYPE:
                pinItrBg = new TextArea();
                pinItrBg.setTheme("msgbox-content");
                pinItrBg.adjustSize();
                pinItrBg.setSize(150, 100);
                pinItrBg.setPosition(cw - 75, ch - 60);
                add(pinItrBg);
                
                msgBoxTitle = new TextArea();
                draggableWidget = msgBoxTitle;
                stmTit = new SimpleTextAreaModel("New absolute point note");
                msgBoxTitle.setModel(stmTit);
                msgBoxTitle.adjustSize();
                msgBoxTitle.setTheme("msgbox-title");
                msgBoxTitle.setSize(150, 20);
                msgBoxTitle.setPosition(cw - 75, ch - 80);
                add(msgBoxTitle);
                
                // Init
                efm = new DefaultEditFieldModel();
                xPinField = new EditField(null, efm);
                xPinField.setText(note != null ? (note.getAbsXVal()+"") : ""); //TODO: read from pinNote
                xPinField.setMultiLine(true);
                xPinField.adjustSize();
                xPinField.setSize(95, 15);
                
                efm = new DefaultEditFieldModel();
                yPinField = new EditField(null, efm);
                yPinField.setText(note != null ? (note.getAbsYVal()+"") : ""); //TODO: read from pinNote
                yPinField.setMultiLine(true);
                yPinField.adjustSize();
                yPinField.setSize(95, 15);
                
                efm = new DefaultEditFieldModel();
                zPinField = new EditField(null, efm);
                zPinField.setText(note != null ? (note.getAbsZVal()+"") : ""); //TODO: read from pinNote
                zPinField.setMultiLine(false);
                zPinField.adjustSize();
                zPinField.setSize(95, 15);
                
                
                xLabelField = new Label("X = ");
                xLabelField.adjustSize();
                xLabelField.setSize(20, 10);
                
                yLabelField = new Label("Y = ");
                yLabelField.adjustSize();
                yLabelField.setSize(20, 10);
                
                zLabelField = new Label("Z = ");
                zLabelField.adjustSize();
                zLabelField.setSize(20, 10);
                
                xLabelField.setPosition(cw - 60, ch - 45);
                xPinField.setPosition(cw - 35, ch - 50);
                
                yLabelField.setPosition(cw - 60, ch - 15);
                yPinField.setPosition(cw - 35, ch - 20);
                
                zLabelField.setPosition(cw - 60, ch + 15);
                zPinField.setPosition(cw - 35, ch + 10);
                
                
                add(xLabelField);
                add(yLabelField);
                add(zLabelField);
                add(xPinField);
                add(yPinField);
                add(zPinField);
                
                
                pinItrOkButton = new Button("OK");
                pinItrOkButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrOkButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       String x = xPinField.getText();
                       String y = yPinField.getText();
                       String z = zPinField.getText();
                       String coo = x + " " + y + " " + z;
                       if (note == null) {
                           note = PinNote.newAbsNote(lastRay[0], lastRay[1], lastRay[2], coo);
                           try {
                               pinPanel.addNew(note);
                           } catch(Exception e) {
                               // TODO Auto-generated catch block
                               e.printStackTrace();
                           }
                           
                       }
                       else {
                           note.setAbs(coo);
                           pinPanel.markChanges();
                       }
                       
                       removeChild(pinItrBg);
                       pinItrBg.destroy();
                       removeChild(xLabelField);
                       xLabelField.destroy();
                       removeChild(yLabelField);
                       yLabelField.destroy();
                       removeChild(zLabelField);
                       zLabelField.destroy();
                       removeChild(xPinField);
                       xLabelField.destroy();
                       removeChild(yPinField);
                       yLabelField.destroy();
                       removeChild(zPinField);
                       zLabelField.destroy();
                       
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                       
                       initPinButtonsEnabled();
                   }
                });
                pinItrOkButton.adjustSize();
                pinItrOkButton.setSize(50, 25);
                pinItrOkButton.setPosition(cw - 25, ch + 50);
                add(pinItrOkButton);
                
                pinItrCancelButton = new Button("Cancel");
                pinItrCancelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrCancelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(xLabelField);
                       xLabelField.destroy();
                       removeChild(yLabelField);
                       yLabelField.destroy();
                       removeChild(zLabelField);
                       zLabelField.destroy();
                       removeChild(xPinField);
                       xLabelField.destroy();
                       removeChild(yPinField);
                       yLabelField.destroy();
                       removeChild(zPinField);
                       zLabelField.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       removeChild(pinItrBg);
                       pinItrBg.destroy();
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                       
                       dialogOpened = false;
                       inputTextMode = false;
                   }
                });
                pinItrCancelButton.adjustSize();
                pinItrCancelButton.setSize(50, 25);
                pinItrCancelButton.setPosition(cw + 25, ch + 50);
                
                add(pinItrCancelButton);
                
                pinItrDelButton = new Button("Delete");
                pinItrDelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrDelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(xLabelField);
                       xLabelField.destroy();
                       removeChild(yLabelField);
                       yLabelField.destroy();
                       removeChild(zLabelField);
                       zLabelField.destroy();
                       removeChild(xPinField);
                       xLabelField.destroy();
                       removeChild(yPinField);
                       yLabelField.destroy();
                       removeChild(zPinField);
                       zLabelField.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       removeChild(pinItrBg);
                       pinItrBg.destroy();
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                       
                       confirmBox("Delete note", "Are you sure you wish to delete the note?", new Runnable() {
                            @Override
                            public void run() {
                                pinPanel.removeNote(note);
                                initPinButtonsEnabled();
                            }
                       }, null);
                       dialogOpened = false;
                       inputTextMode = false;
                   }
                });
                pinItrDelButton.adjustSize();
                pinItrDelButton.setSize(50, 25);
                if(note == null) {
                    pinItrDelButton.setVisible(false);
                }
                pinItrDelButton.setPosition(cw - 75, ch + 50);
                
                add(pinItrDelButton);
                
                break;
            case PinNote.IMAGE_TYPE:
                if (note == null) {
                    loadingImage = true;
                    fileSelector.setVisible(true);
                }
                else {
                    showImage(note, true, false);
                }
                
                break;
                
            case PinNote.TEXT_TYPE:
                efm = new DefaultEditFieldModel();
                textPinField = new EditField(null, efm);
                textPinField.setText(note != null ? note.getTextValue() : ""); //TODO: read from pinNote
                textPinField.setMultiLine(true);
                textPinField.adjustSize();
                
                textPinField.setCanAcceptKeyboardFocus(true);
                
                pinItrPane = new ScrollPane(textPinField);
                pinItrPane.setFixed(ScrollPane.Fixed.VERTICAL);
                pinItrPane.setExpandContentSize(true);
                pinItrPane.setTheme("scrollpane");
                pinItrPane.setVisible(true);
                textPinField.adjustSize();
                
                pinItrPane.setSize(400, 400);
                pinItrPane.setPosition(cw - 200, ch - 200);
                
                msgBoxTitle = new TextArea();
                draggableWidget = msgBoxTitle;
                stmTit = new SimpleTextAreaModel("New text note");
                msgBoxTitle.setModel(stmTit);
                msgBoxTitle.adjustSize();
                msgBoxTitle.setTheme("msgbox-title");
                msgBoxTitle.setSize(400, 20);
                msgBoxTitle.setPosition(cw - 200, ch - 220);
                add(msgBoxTitle);
                
                add(pinItrPane);
                
                pinItrOkButton = new Button("OK");
                pinItrOkButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrOkButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       String text = textPinField.getText();
                       // TODO: get coordinates
                       if(note == null) {
                           note = PinNote.newTextNote(lastRay[0], lastRay[1], lastRay[2], text); 
                           try {
                               pinPanel.addNew(note);
                           } catch(Exception e) {
                               // TODO Auto-generated catch block
                               e.printStackTrace();
                           }
                       }
                       else {
                           note.setTextValue(text);
                           note.markUnsynced();
                           try {
                               pinPanel.sync();
                           } catch(IOException e) {
                               // TODO Auto-generated catch block
                               e.printStackTrace();
                           }
                       }
                       
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                       
                       initPinButtonsEnabled();
                   }
                });
                pinItrOkButton.adjustSize();
                pinItrOkButton.setSize(50, 25);
                pinItrOkButton.setPosition(cw + 50, ch + 200);
                add(pinItrOkButton);
                
                pinItrCancelButton = new Button("Cancel");
                pinItrCancelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrCancelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                   }
                });
                pinItrCancelButton.adjustSize();
                pinItrCancelButton.setSize(50, 25);
                pinItrCancelButton.setPosition(cw + 100, ch + 200);
                
                add(pinItrCancelButton);
                
                pinItrDelButton = new Button("Delete");
                pinItrDelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrDelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       

                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                       
                       confirmBox("Delete note", "Are you sure you wish to delete the note?", new Runnable() {
                            @Override
                            public void run() {
                                pinPanel.removeNote(note);
                                initPinButtonsEnabled();
                            }
                       }, null);
                   }
                });
                pinItrDelButton.adjustSize();
                pinItrDelButton.setSize(50, 25);
                if(note == null) {
                    pinItrDelButton.setVisible(false);
                }
                pinItrDelButton.setPosition(cw, ch + 200);
                
                add(pinItrDelButton);
                
                break;
                
            case PinNote.DEFAULT_TYPE:
            default:
                
                break;
        }
        
        initPinButtonsEnabled();
    }
    
    
    private void showImage(PinNote n, boolean edit, boolean isNew) {
        imageWidget = new ImageWidget();
        BufferedImage img;
        
        int cw = settings.resWidth/2;
        int ch = settings.resHeight/2;
        try {
            img = n.getImageValue();
            ByteBuffer bb = ImageOp.getImageDataFromImage(img);
            DynamicImage di = renderer.createDynamicImage(img.getWidth(), img.getHeight()); 
            
            di.update(bb, DynamicImage.Format.RGBA);
            
            int left = 400 / 2;
            int up = 400 / 2;
            
            imageWidget.setImage(di, img);            
            n.clearImage();
            note = n;
            
            //add(imageWidget);
            
            msgBoxTitle = new TextArea();
            draggableWidget = msgBoxTitle;
            SimpleTextAreaModel stmTit = new SimpleTextAreaModel(note.getName());
            msgBoxTitle.setModel(stmTit);
            msgBoxTitle.adjustSize();
            msgBoxTitle.setTheme("msgbox-title");
            msgBoxTitle.setSize(150, 20);
            msgBoxTitle.setPosition(cw - left, ch - up - 40);
            add(msgBoxTitle);
            
            pinItrPane = new ScrollPane(imageWidget);
            pinItrPane.setFixed(ScrollPane.Fixed.NONE);
            pinItrPane.setExpandContentSize(true);
            pinItrPane.setTheme("scrollpane");
            pinItrPane.setVisible(true);
            //textPinField.adjustSize();
            pinItrPane.adjustSize();
            pinItrPane.setSize(400, 400);
            pinItrPane.setPosition(cw - left, ch - up - 20);
            
            add(pinItrPane);
            
            if(edit) {
                pinItrOkButton = new Button("Save");
                pinItrOkButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrOkButton.addCallback(new Runnable(){
                   @Override
                   public void run(){
                       try {
                       pinPanel.addNew(note);
                       } catch(Exception e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       }
                       
                       
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                       
                       initPinButtonsEnabled();
                   }
                });
                pinItrOkButton.adjustSize();
                pinItrOkButton.setSize(50, 25);
                pinItrOkButton.setPosition(cw + left - 110, ch + up);
                if(! isNew) pinItrOkButton.setEnabled(false);
                add(pinItrOkButton);
                
                pinItrCancelButton = new Button("Cancel");
                pinItrCancelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrCancelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                   }
                });
                pinItrCancelButton.adjustSize();
                pinItrCancelButton.setSize(50, 25);
                pinItrCancelButton.setPosition(cw + left - 50, ch + up);
                
                add(pinItrCancelButton);
                
                pinItrDelButton = new Button("Delete");
                pinItrDelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrDelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrOkButton);
                       pinItrOkButton.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrDelButton);
                       pinItrDelButton.destroy();
                       
                       confirmBox("Delete note", "Are you sure you wish to delete the note?", new Runnable() {
                            @Override
                            public void run() {
                                pinPanel.removeNote(note);
                                initPinButtonsEnabled();
                            }
                       }, null);
                       dialogOpened = false;
                       inputTextMode = false;
                   }
                });
                pinItrDelButton.adjustSize();
                pinItrDelButton.setSize(50, 25);
                if(isNew) {
                    pinItrDelButton.setVisible(false);
                }
                pinItrDelButton.setPosition(cw + left - 170, ch + up);
                
                add(pinItrDelButton);
            }
            else {
                pinItrCancelButton = new Button("Close");
                pinItrCancelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrCancelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                   }
                });
                pinItrCancelButton.adjustSize();
                pinItrCancelButton.setSize(50, 25);
                pinItrCancelButton.setPosition(cw + left - 50, ch + up);
                
                add(pinItrCancelButton);
            }
            
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void showPinNote() {
        if(pinPanel == null) return;
        note = pinPanel.getNearest(lastRay[0], lastRay[1], lastRay[2]); 
        
        if (note != null && note.distanceTo(lastRay[0], lastRay[1], lastRay[2]) > MAX_RAY_DISTANCE) {
            note = null;
        }
        if (note == null) return;
        
        dialogOpened = true; 

        inputTextMode = true;
        
        int cw = settings.resWidth/2;
        int ch = settings.resHeight/2;
        SimpleTextAreaModel stmTit;
        switch (note.getType()) {
            case PinNote.ABSOLUTE_TYPE:
                pinItrBg = new TextArea();
                pinItrBg.setTheme("msgbox-content");
                pinItrBg.adjustSize();
                pinItrBg.setSize(150, 100);
                pinItrBg.setPosition(cw - 75, ch - 60);
                add(pinItrBg);
                
                msgBoxTitle = new TextArea();
                draggableWidget = msgBoxTitle;
                stmTit = new SimpleTextAreaModel(note.getName());
                msgBoxTitle.setModel(stmTit);
                msgBoxTitle.adjustSize();
                msgBoxTitle.setTheme("msgbox-title");
                msgBoxTitle.setSize(150, 20);
                msgBoxTitle.setPosition(cw - 75, ch - 80);
                add(msgBoxTitle);
                
                // Init
                
                xLabelField = new Label("X = " + note.getAbsXVal());
                xLabelField.adjustSize();
                xLabelField.setSize(140, 10);
                
                yLabelField = new Label("Y = " + note.getAbsYVal());
                yLabelField.adjustSize();
                yLabelField.setSize(140, 10);
                
                zLabelField = new Label("Z = " + note.getAbsZVal());
                zLabelField.adjustSize();
                zLabelField.setSize(140, 10);
                
                xLabelField.setPosition(cw - 70, ch - 45);
                
                yLabelField.setPosition(cw - 70, ch - 15);
                
                zLabelField.setPosition(cw - 72, ch + 15);
                
                
                add(xLabelField);
                add(yLabelField);
                add(zLabelField);
                
                
                pinItrCancelButton = new Button("Cancel");
                pinItrCancelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrCancelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(xLabelField);
                       xLabelField.destroy();
                       removeChild(yLabelField);
                       yLabelField.destroy();
                       removeChild(zLabelField);
                       zLabelField.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       removeChild(pinItrBg);
                       pinItrBg.destroy();
                       
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                   }
                });
                pinItrCancelButton.adjustSize();
                pinItrCancelButton.setSize(50, 25);
                pinItrCancelButton.setPosition(cw + 25, ch + 50);
                
                add(pinItrCancelButton);
                
                
                break;
            case PinNote.IMAGE_TYPE:
                showImage(note, false, false);
                break;
                
            case PinNote.TEXT_TYPE:
                EditFieldModel efm = new DefaultEditFieldModel();
                textPinField = new EditField(null, efm);
                textPinField.setText(note != null ? note.getTextValue() : "");
                textPinField.setMultiLine(true);
                textPinField.adjustSize();
                textPinField.setReadOnly(true);
                textPinField.setCanAcceptKeyboardFocus(true);
                
                msgBoxTitle = new TextArea();
                draggableWidget = msgBoxTitle;
                stmTit = new SimpleTextAreaModel(note.getName());
                msgBoxTitle.setModel(stmTit);
                msgBoxTitle.adjustSize();
                msgBoxTitle.setTheme("msgbox-title");
                msgBoxTitle.setSize(400, 20);
                msgBoxTitle.setPosition(cw - 200, ch - 220);
                add(msgBoxTitle);
                
                pinItrPane = new ScrollPane(textPinField);
                pinItrPane.setFixed(ScrollPane.Fixed.VERTICAL);
                pinItrPane.setExpandContentSize(true);
                pinItrPane.setTheme("scrollpane");
                pinItrPane.setVisible(true);
                textPinField.adjustSize();
                
                pinItrPane.setSize(400, 400);
                pinItrPane.setPosition(settings.resWidth/2 - 200, settings.resHeight/2 - 200);
                
                add(pinItrPane);
                
                pinItrCancelButton = new Button("Close");
                pinItrCancelButton.setTheme("button");
                //pinItrOkButton.setTooltipContent("Open the dialog with the file chooser to select an .r3dp file.");
                pinItrCancelButton.addCallback(new Runnable(){
                   @Override
                public void run(){
                       removeChild(pinItrPane);
                       pinItrPane.destroy();
                       removeChild(pinItrCancelButton);
                       pinItrCancelButton.destroy();
                       dialogOpened = false;
                       inputTextMode = false;
                       
                       removeChild(msgBoxTitle);
                       msgBoxTitle.destroy();
                       msgBoxTitle = null;
                       draggableWidget = null;
                   }
                });
                pinItrCancelButton.adjustSize();
                pinItrCancelButton.setSize(50, 25);
                pinItrCancelButton.setPosition(settings.resWidth/2, settings.resHeight/2 + 200);
                
                add(pinItrCancelButton);
                
                break;
                
            case PinNote.DEFAULT_TYPE:
            default:
            
                break;
        }
        
    }
    
    /**
     * @since 0.1
     * @version 0.1
     */
    private static void logic(){
        //update framerate and calculate time that passed since last frame
        long time=(Sys.getTime()*1000)/Sys.getTimerResolution();
        fps++;
        if(time-timePastFps>=1000){
            fpsToDisplay=fps;
            fps=0;
            timePastFps=time;
            cameraOrientation=Quaternion.quaternionNormalization(cameraOrientation);
            if(openedModel!=null){
                addedModelOrientation=Quaternion.quaternionNormalization(addedModelOrientation);
                currentModelOrientation=Quaternion.quaternionNormalization(currentModelOrientation);
            }
        }
        timePastFrame=time;
        
    }
    public static void exitProgram(int n){
        saveSettings();
        for(int i=0;i<NUMBER_OF_SHADER_PROGRAMS;i++){
            glDetachShader(shaderPrograms[i], vertexShaders[i]);
            glDeleteShader(vertexShaders[i]);
            glDetachShader(shaderPrograms[i], fragmentShaders[i]);
            glDeleteShader(fragmentShaders[i]);
            glDeleteProgram(shaderPrograms[i]);
        }
        gui.destroy();
        if(themeManager!=null)themeManager.destroy();
        Display.destroy();
        System.exit(n);
    }
    public static void prepareShaders(){
        shaderPrograms= new int[NUMBER_OF_SHADER_PROGRAMS];
        vertexShaders= new int[NUMBER_OF_SHADER_PROGRAMS];
        fragmentShaders= new int[NUMBER_OF_SHADER_PROGRAMS];
        String path="/main/";
        for(int i=1;i<=NUMBER_OF_SHADER_PROGRAMS;i++){
            shaderPrograms[i-1] = GL20.glCreateProgram();
            vertexShaders[i-1] = glCreateShader(GL_VERTEX_SHADER);
            fragmentShaders[i-1] = glCreateShader(GL_FRAGMENT_SHADER);
            StringBuilder vertexShaderSource = new StringBuilder();
            StringBuilder fragmentShaderSource= new StringBuilder();
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(InputStreamReader.class.getResourceAsStream(path+"shader"+i+".vert")));
                String line;
                while( (line=reader.readLine())!=null){
                    vertexShaderSource.append(line).append("\n");
                }
                reader.close();
            }catch(IOException e){
                System.err.println("Vertex shader"+i+" wasn't loaded properly.");
                exitProgram(1);
            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(InputStreamReader.class.getResourceAsStream(path+"shader"+i+".frag")));
                String line;
                while( (line=reader.readLine())!=null){
                    fragmentShaderSource.append(line).append("\n");
                }
                reader.close();
            }catch(IOException e){
                System.err.println("Fragment shader"+i+" wasn't loaded properly.");
                exitProgram(1);
            }
            glShaderSource(vertexShaders[i-1], vertexShaderSource);
            glCompileShader(vertexShaders[i-1]);
            if(glGetShader(vertexShaders[i-1], GL_COMPILE_STATUS)==GL_FALSE){
                System.err.println("Vertex shader"+i+" not compiled correctly");
            }
            
            glShaderSource(fragmentShaders[i-1], fragmentShaderSource);
            glCompileShader(fragmentShaders[i-1]);
            if(glGetShader(fragmentShaders[i-1], GL_COMPILE_STATUS)==GL_FALSE){
                System.err.println("Fragment shader"+i+" not compiled correctly");
            }
            
            glAttachShader(shaderPrograms[i-1], vertexShaders[i-1]);
            glAttachShader(shaderPrograms[i-1], fragmentShaders[i-1]);
            glLinkProgram(shaderPrograms[i-1]);
            glValidateProgram(shaderPrograms[i-1]);
            
            System.out.println("Vertex shader"+i+" info: "+glGetShaderInfoLog(vertexShaders[i-1], 999));
            System.out.println("Fragment shader"+i+" info: "+glGetShaderInfoLog(fragmentShaders[i-1], 999));
            System.out.println("Shader program"+i+" info: "+glGetShaderInfoLog(shaderPrograms[i-1], 999));
        }
    }
    
    /**
     * @param args - has no function
     * @since 0.1
     * @version 0.4
     */
    public static void main(String[] args) {
        cameraOrientation=new Quaternion();
        themeManager=null;
        boolean loadSuccessful;
        try{
            displayModes=Display.getAvailableDisplayModes();
            displayModeStrings=new String[displayModes.length];
            
            loadSuccessful=loadSettings();
            currentDisplayMode=Display.getDesktopDisplayMode();
            if(loadSuccessful){
                for(DisplayMode mode:displayModes){
                    if(mode.getWidth()==settings.resWidth && mode.getHeight()==settings.resHeight && mode.getBitsPerPixel()==settings.bitsPerPixel && mode.getFrequency()==settings.frequency){
                        currentDisplayMode=mode;
                    }
                }
            }else{
                settings=new NeckVeinsSettings();
                settings.isFpsShown=false;
                settings.fullscreen=true;
                settings.stereoEnabled=false;
                settings.stereoValue=0;
            }
            settings.resWidth=currentDisplayMode.getWidth();
            settings.resHeight=currentDisplayMode.getHeight();
            settings.bitsPerPixel=currentDisplayMode.getBitsPerPixel();
            settings.frequency=currentDisplayMode.getFrequency();
            Display.setDisplayMode(currentDisplayMode);
            Display.setTitle(title);
            Display.create(new PixelFormat().withStencilBits(1));
            //Display.create();
            Display.setVSyncEnabled(true);
            
            GL11.glClearStencil(0);
            
            //The TWL part
            renderer = new LWJGLRenderer();
            gameUI = new MainFrame();
            gui = new GUI(gameUI, renderer);
            themeManager = ThemeManager.createThemeManager(
                    MainFrame.class.getResource("simple.xml"), renderer);
            gui.applyTheme(themeManager);
            setupView();
            renderer.syncViewportSize();
            gameUI.invalidateLayout();
        }catch(LWJGLException e){
            e.printStackTrace();
            exitProgram(1);
        }catch(IOException ee){
            ee.printStackTrace();
            exitProgram(1);
        }
        System.out.println("OPENGL VERSION: "+GL11.glGetString(GL11.GL_VERSION)+"\n");
        prepareShaders();
        
        isRunning=true;
        mainLoop();
        exitProgram(0);
    }
    
    // User messaging section
    
    public void infoBox(String title, String message) {
        dialogOpened = true;
        
        msgBoxContent = new TextArea();
        msgBoxTitle = new TextArea();
        draggableWidget = msgBoxTitle;
        
        SimpleTextAreaModel stmMsg = new SimpleTextAreaModel(message);
        SimpleTextAreaModel stmTit = new SimpleTextAreaModel(title);
        
        
        msgBoxContent.setModel(stmMsg);
        msgBoxTitle.setModel(stmTit);
        
        StyleSheet css = new StyleSheet();
        try {
            css.parse("p,div { text-align: center; }");
            msgBoxTitle.setStyleClassResolver(css);
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        msgBoxContent.adjustSize();
        msgBoxTitle.adjustSize();
        
        msgBoxTitle.setTheme("msgbox-title");
        msgBoxContent.setTheme("msgbox-content");
        
        msgBoxTitle.setSize(200, 20);
        msgBoxContent.setSize(200, 20);
        
        msgBoxTitle.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 20);
        msgBoxContent.setPosition(settings.resWidth/2 - 100, settings.resHeight/2);
        
        
        add(msgBoxTitle);
        add(msgBoxContent);
        
        //inputTextMode = false;
    }
    

    public void alertBox(String title, String message) {
        dialogOpened = true;
        
        msgBoxContent = new TextArea();
        msgBoxTitle = new TextArea();
        draggableWidget = msgBoxTitle;
        
        SimpleTextAreaModel stmMsg = new SimpleTextAreaModel(message);
        SimpleTextAreaModel stmTit = new SimpleTextAreaModel(title);
        
        
        msgBoxContent.setModel(stmMsg);
        msgBoxTitle.setModel(stmTit);
        
        StyleSheet css = new StyleSheet();
        try {
            css.parse("p,div { text-align: center; }");
            msgBoxTitle.setStyleClassResolver(css);
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        msgBoxContent.adjustSize();
        msgBoxTitle.adjustSize();
        
        msgBoxTitle.setTheme("msgbox-title");
        msgBoxContent.setTheme("msgbox-content");
        
        msgBoxTitle.setSize(200, 20);
        msgBoxContent.setSize(200, 20);
        
        msgBoxTitle.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 40);
        msgBoxContent.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 20);
        
        msgBoxCloseButton = new Button("Close");
        msgBoxCloseButton.setPosition(settings.resWidth/2 - 50, settings.resHeight/2);
        msgBoxCloseButton.setSize(100, 40);
        msgBoxCloseButton.addCallback(new Runnable() {
            public void run() {
                setButtonsEnabled(true);
                msgBoxDestroy();
            }
        });
        
        add(msgBoxCloseButton);
        add(msgBoxTitle);
        add(msgBoxContent);
        
        //inputTextMode = false;
    }
    
    public void confirmBox(String title, String message, Runnable okFunction, Runnable cancelFunction) {
        dialogOpened = true;
        
        msgBoxContent = new TextArea();
        msgBoxTitle = new TextArea();
        draggableWidget = msgBoxTitle;
        
        SimpleTextAreaModel stmMsg = new SimpleTextAreaModel(message);
        SimpleTextAreaModel stmTit = new SimpleTextAreaModel(title);
        
        
        msgBoxContent.setModel(stmMsg);
        msgBoxTitle.setModel(stmTit);
        
        msgBoxContent.adjustSize();
        msgBoxTitle.adjustSize();
        
        msgBoxTitle.setTheme("msgbox-title");
        msgBoxContent.setTheme("msgbox-content");
        
        msgBoxTitle.setSize(200, 20);
        msgBoxContent.setSize(200, 20);
        
        msgBoxTitle.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 40);
        msgBoxContent.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 20);

        
        msgBoxOkButton = new Button("OK");
        msgBoxOkButton.setPosition(settings.resWidth/2 - 100, settings.resHeight/2);
        msgBoxOkButton.setSize(100, 40);
        
        msgBoxCancelButton = new Button("Cancel");
        msgBoxCancelButton.setPosition(settings.resWidth/2, settings.resHeight/2);
        msgBoxCancelButton.setSize(100, 40);       

        
        // Callbacks
        msgBoxOkButton.addCallback(new Runnable() {
            public void run() {
                setButtonsEnabled(true);
                msgBoxDestroy();
            }
        });
        if (okFunction != null) {
            msgBoxOkButton.addCallback(okFunction);
        }
        msgBoxCancelButton.addCallback(new Runnable() {
            public void run() {
                msgBoxDestroy();
                setButtonsEnabled(true);
            }
        });
        if (cancelFunction != null) {
            msgBoxCancelButton.addCallback(cancelFunction);
        }
        
        add(msgBoxCancelButton);
        add(msgBoxOkButton);
        add(msgBoxTitle);
        add(msgBoxContent);
    }
    
    public void inputBox(String title, String message, String inInput, Runnable okFunction, Runnable cancelFunction) {
        dialogOpened = true;
        
        SimpleTextAreaModel stmMsg = new SimpleTextAreaModel(message);
        SimpleTextAreaModel stmTit = new SimpleTextAreaModel(title);
        
        EditFieldModel stmInput = new DefaultEditFieldModel();
        msgBoxContent = new TextArea();
        msgBoxTitle = new TextArea();
        draggableWidget = msgBoxTitle;
        msgBoxInput = new EditField(null, stmInput);
        msgBoxInput.setText(inInput == null ? "" : inInput);
        msgBoxContent.setModel(stmMsg);
        msgBoxTitle.setModel(stmTit);
        //msgBoxInput.setModel(stmInput);
        
        msgBoxInput.setEnabled(true);
        msgBoxInput.setMultiLine(true);
        
        msgBoxContent.adjustSize();
        msgBoxTitle.adjustSize();
        msgBoxInput.adjustSize();
        
        msgBoxTitle.setTheme("msgbox-title");
        msgBoxContent.setTheme("msgbox-content");
        
        msgBoxTitle.setSize(200, 20);
        msgBoxContent.setSize(200, 20);
        msgBoxInput.setSize(200, 20);
        
        msgBoxInput.setEnabled(true);
        msgBoxInput.setCanAcceptKeyboardFocus(true);
        
        msgBoxTitle.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 40);
        msgBoxContent.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 20);
        msgBoxInput.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 0);
        
        msgBoxOkButton = new Button("OK");
        msgBoxOkButton.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 + 20);
        msgBoxOkButton.setSize(100, 40);       
        
        msgBoxCancelButton = new Button("Cancel");
        msgBoxCancelButton.setPosition(settings.resWidth/2, settings.resHeight/2 + 20);
        msgBoxCancelButton.setSize(100, 40);
        
        if (okFunction != null) {
            msgBoxOkButton.addCallback(okFunction);
        }
        
        msgBoxOkButton.addCallback(new Runnable() {
            public void run() {
                msgBoxDestroy();
                inputTextMode = false;
                setButtonsEnabled(true);
            }
        });
        
        if (cancelFunction != null) {
            msgBoxCancelButton.addCallback(cancelFunction);
        }
        msgBoxCancelButton.addCallback(new Runnable() {
            public void run() {
                msgBoxDestroy();
                setButtonsEnabled(true);
                inputTextMode = false;
            }
        });
        
        
        add(msgBoxCancelButton);
        add(msgBoxOkButton);
        add(msgBoxTitle);
        add(msgBoxInput);
        add(msgBoxContent);
        
        
        inputTextMode = true;
    }
    
    public void msgBoxDestroy() {
        dialogOpened = false;
        inputTextMode = false;
        if (msgBoxContent != null) {
            removeChild(msgBoxContent);
            msgBoxContent.destroy();
            msgBoxContent = null;
        }
        if (msgBoxTitle != null) {
            removeChild(msgBoxTitle);
            msgBoxTitle.destroy();
            msgBoxTitle = null;
        }
        if (msgBoxCloseButton != null) {
            removeChild(msgBoxCloseButton);
            msgBoxCloseButton.destroy();
            msgBoxCloseButton = null;
        }
        if (msgBoxInput != null) {
            removeChild(msgBoxInput);
            msgBoxInput.destroy();
            msgBoxInput = null;
        }
        if (msgBoxOkButton != null) {
            removeChild(msgBoxOkButton);
            msgBoxOkButton.destroy();
            msgBoxOkButton = null;
        }
        if (msgBoxCancelButton != null) {
            removeChild(msgBoxCancelButton);
            msgBoxCancelButton.destroy();
            msgBoxCancelButton = null;
        }
        draggableWidget = null;
    }
    
    public void moveDialogs(int dX, int dY) {
        draggableWidget.setPosition(draggableWidget.getX() + dX, draggableWidget.getY() + dY);
        
        if (pinItrPane != null) 
            pinItrPane.setPosition(pinItrPane.getX() + dX, pinItrPane.getY() + dY);
        if (msgBoxContent != null) 
            msgBoxContent.setPosition(msgBoxContent.getX() + dX, msgBoxContent.getY() + dY);
        if (msgBoxInput != null) 
            msgBoxInput.setPosition(msgBoxInput.getX() + dX, msgBoxInput.getY() + dY);
        if (msgBoxOkButton != null) 
            msgBoxOkButton.setPosition(msgBoxOkButton.getX() + dX, msgBoxOkButton.getY() + dY);
        if (msgBoxCloseButton != null) 
            msgBoxCloseButton.setPosition(msgBoxCloseButton.getX() + dX, msgBoxCloseButton.getY() + dY);
        if (msgBoxCancelButton != null) 
            msgBoxCancelButton.setPosition(msgBoxCancelButton.getX() + dX, msgBoxCancelButton.getY() + dY);
        if (textPinField != null && false) 
            textPinField.setPosition(textPinField.getX() + dX, textPinField.getY() + dY);
        if (xLabelField != null) 
            xLabelField.setPosition(xLabelField.getX() + dX, xLabelField.getY() + dY);
        if (xPinField != null) 
            xPinField.setPosition(xPinField.getX() + dX, xPinField.getY() + dY);
        if (yLabelField != null) 
            yLabelField.setPosition(yLabelField.getX() + dX, yLabelField.getY() + dY);
        if (yPinField != null) 
            yPinField.setPosition(yPinField.getX() + dX, yPinField.getY() + dY);
        if (zLabelField != null) 
            zLabelField.setPosition(zLabelField.getX() + dX, zLabelField.getY() + dY);
        if (zPinField != null) 
            zPinField.setPosition(zPinField.getX() + dX, zPinField.getY() + dY);
        if (pinItrOkButton != null) 
            pinItrOkButton.setPosition(pinItrOkButton.getX() + dX, pinItrOkButton.getY() + dY);
        if (pinItrDelButton != null) 
            pinItrDelButton.setPosition(pinItrDelButton.getX() + dX, pinItrDelButton.getY() + dY);
        if (pinItrCancelButton != null) 
            pinItrCancelButton.setPosition(pinItrCancelButton.getX() + dX, pinItrCancelButton.getY() + dY);
        if (pinItrBg != null) 
            pinItrBg.setPosition(pinItrBg.getX() + dX, pinItrBg.getY() + dY);
        
        
        if (msgBoxTitle != null && msgBoxTitle != draggableWidget) {
            msgBoxTitle.setPosition(msgBoxTitle.getX() + dX, msgBoxTitle.getY() + dY);
        }
        
    }
    
public void displayImage(String title, String message) {
        
        msgBoxContent = new TextArea();
        msgBoxTitle = new TextArea();
        
        SimpleTextAreaModel stmMsg = new SimpleTextAreaModel(message);
        SimpleTextAreaModel stmTit = new SimpleTextAreaModel(title);
        
        
        msgBoxContent.setModel(stmMsg);
        msgBoxTitle.setModel(stmTit);
        
        StyleSheet css = new StyleSheet();
        try {
            css.parse("p,div { text-align: center; }");
            msgBoxTitle.setStyleClassResolver(css);
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        msgBoxContent.adjustSize();
        msgBoxTitle.adjustSize();
        
        msgBoxTitle.setTheme("msgbox-title");
        msgBoxContent.setTheme("msgbox-content");
        
        msgBoxTitle.setSize(200, 20);
        msgBoxContent.setSize(200, 20);
        
        msgBoxTitle.setPosition(settings.resWidth/2 - 100, settings.resHeight/2 - 20);
        msgBoxContent.setPosition(settings.resWidth/2 - 100, settings.resHeight/2);
        
        
        add(msgBoxTitle);
        add(msgBoxContent);
        
        inputTextMode = true;
    }
}
