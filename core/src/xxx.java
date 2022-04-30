//package com.gsr.screen;
//
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.NinePatch;
//import com.badlogic.gdx.graphics.g2d.ParticleEffect;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Interpolation;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Action;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.Touchable;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.Align;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.SnapshotArray;
//import com.badlogic.gdx.utils.Timer;
//import com.esotericsoftware.spine.AnimationState;
//import com.esotericsoftware.spine.Bone;
//import com.esotericsoftware.spine.Event;
//import com.esotericsoftware.spine.SkeletonData;
//import com.gsr.PopupManager;
//import com.gsr.RuntimeData;
//import com.gsr.actions.BezierMoveAction;
//import com.gsr.assets.Assets;
//import com.gsr.assets.GameplayAssets;
//import com.gsr.assets.WordData;
//import com.gsr.data.ChapterUtil;
//import com.gsr.data.Constants;
//import com.gsr.data.GameData;
//import com.gsr.data.MyEnum;
//import com.gsr.data.Prefs;
//import com.gsr.data.StateManager;
//import com.gsr.data.UserProbability;
//import com.gsr.loader.Text;
//import com.gsr.managers.AudioManager;
//import com.gsr.managers.PlatformManager;
//import com.gsr.spineActor.SpineGroup;
//import com.gsr.struct.DailyNotCompleteStruct;
//import com.gsr.struct.Position;
//import com.gsr.struct.TrieTree;
//import com.gsr.ui.button.GamePlayButton;
//import com.gsr.ui.button.LevelButton;
//import com.gsr.ui.button.SpineGroupButton;
//import com.gsr.ui.button.ZoomButton;
//import com.gsr.ui.groups.MyLine;
//import com.gsr.ui.groups.preview.Preview;
//import com.gsr.ui.panels.AllWordPanel;
//import com.gsr.ui.panels.ClickWordPanel;
//import com.gsr.ui.panels.CoinSupplyPanel;
//import com.gsr.ui.panels.DailyGuidePanel;
//import com.gsr.ui.panels.DailyPanel;
//import com.gsr.ui.panels.ExtraWordPanel;
//import com.gsr.ui.panels.ExtraWordPanel2;
//import com.gsr.ui.panels.LevelPassPanel;
//import com.gsr.ui.panels.NovicesRewardPanel;
//import com.gsr.ui.panels.Panel;
//import com.gsr.ui.panels.PropRewardPanel;
//import com.gsr.ui.panels.RatePanel;
//import com.gsr.ui.panels.SpellingBeePanel;
//import com.gsr.ui.panels.StrikePanel;
//import com.gsr.ui.panels.YindaoPanel;
//import com.gsr.ui.someactor.DailyProgress;
//import com.gsr.ui.someactor.Gemstone;
//import com.gsr.ui.someactor.ParticleEffectActor;
//import com.gsr.ui.someactor.YanhuaGroup;
//import com.gsr.ui.someactor.YindaoXuxian;
//import com.gsr.utils.BoxUtils;
//import com.gsr.utils.ButtonClickListener;
//import com.gsr.utils.CalendarUtils;
//import com.gsr.utils.ConvertUtil;
//import com.gsr.utils.LevelUtil;
//import com.gsr.utils.MyMathUtils;
//import com.gsr.utils.SpellingBeeManager;
//import com.gsr.utils.ViewportUtils;
//import com.gsr.wordcross.MyGame;
//import com.json.PythonDict;
//import com.qs.ui.ManagerUIEditor;
//import com.qs.ui.plist.PlistAtlas;
//
//import java.util.Comparator;
//import java.util.TreeSet;
//
//import static com.badlogic.gdx.math.MathUtils.random;
//
//public class GameplayScreen extends BaseScreen {
//
//    private boolean debug = false;
//    boolean useDNALevel = false;
//    String DDNALeverVersion = "local";
//    //第几章的第几关
//    int levelIs;
//    String title;
//
//
//    //本关额外单词
//    String extraWord[];
//
//    //引导要连的单词的序号，暂时只用来给每日挑战的引导使用
//    int yindaoWordIndex;
//    int gameIs;
//    int gameSolved;
//
//    //可以忽视遮罩的参数
//    //使用地方如第4关手指提示引导的时候，为了能点格子需要将其设置为true
//    boolean canIgnoreMask = false;
//
//    //是否可以连线
//    public boolean canLianxian = true;
//
//
//    //连接格子动画的时间,跟动画时间改变
//    final float linkGeziDelay = 18 * Constants.AnimationGap;
//    //连接一个单词的时候每个格子间的延迟
//    final float lightGeziDelay = 2 * Constants.AnimationGap;
//
//    //连续答对的次数，不算额外单词的答对
//    int correctTimes = 0;
//    //连续答错次数，只算连出3个及以上长度的词语时计算
//    int actualErrorTimes = 0;
//    //连续答错次数，只算连出3个及以上长度的词语时计算,包括重复次数
//    int errorTimes = 0;
//    //因为goJiesuan是延迟后调用，所以可能会调用多次，只要点的快的话
//    boolean isJiesuan;
//
//
//    //连击增加的次数，用于点亮单一格子后使用
//    int addComboTimes;
//    //闪电提示用来存要点亮字母的数组
//    Position fasthintArr[];
//    //闪电提示用来存单一单词的空格子
//    Position fasthintWordArr[];
//    //枫叶位置
//    Position fengPosition;
//    TreeSet notPassSet = new TreeSet<Integer>();
//
//    //空着的格子
//    BoxUtils<Position> emptyBox;
//
//    Position tPosition = new Position(-1, -1, -1, -1);
//    String stateStr, hintAppearStr, hintIndexStr, extraStateStr;
//
//
//    ManagerUIEditor managerUIEditor, gameplayButtonUIEditor;
//    Group baseGroup;
//
//    GameData gameData;
//
//    private SpineGroup fingerSpineGroup;
//
//    private boolean checkStartLevelPopup = false;
//
//    private InputListener inputListener;
//    private long startTime;
//    private long endTime;
//    private MyEnum.GameMode gameMode;
//
//    public class GridMsg {
//        public int vIndex, vPos, vLength;
//        public int hIndex, hPos, hLength;
//        public boolean has;
//        public char ch;
//
//        MyEnum.WordAppear appear;
//
//        public GridMsg() {
//            vIndex = -1;
//            hIndex = -1;
//            appear = MyEnum.WordAppear.empty;
//            has = false;
//        }
//    }
//
//    public class WordMsg {
//        public String str;
//
//        public int x, y, length;
//        MyEnum.Dir dir;
//
//        public WordMsg() {
//        }
//
//    }
//
//    //点击字母判定的缩放区域
//    float touchScaleX = 0.7f, touchScaleY = 0.7f;
//
//    //木块的宽度和高度
//    float woodWidth, woodHeight;
//    //每日挑战的进度
//    int dailyJindu;
//    //每关的金币单词
//    String coinWord;
//    //判断是不是新的关卡，即需不需要考虑state
//    boolean isNewLevel;
//
//
//    boolean isYindao = false;
//
//    //判断是不是不需要考虑state的引导状态
//    boolean isYindaoWithoutState;
//    //判断是不是在第一关和每日挑战刚开始的引导，这个时候不能响应onback
//    boolean isWordYindao;
//    boolean isFinger;
//    //单词总数
//    int wordCount;
//    int letterLength;
//    String gameLetter;
//    String[] gameAnswer;
//    boolean[] isFound;
//    //	float lettersScale;
//    int hintId;
//    int[] hintIndex;
//    int[] hintLength;
//
//    int passWordState;
//    //当前已经找到的单词状态
//    int wordState;
//    int skinState;
//    int w, h;
//    //格子尺寸
//    float geziSizeY, geziSizeX;
//    float halfGeziSizeY, halfGeziSizeX;
//    float useImgSizeX, useImgSizeY;
//    String words[];
//    boolean wordUsed[];
//    //分别是竖直摆放和水平摆放
//    GridMsg gridMsg[][];
//    WordMsg wordMsg[];
//
//    //引导虚线相关
//    int zimuShunxu[];
//
//
//    private SpineGroup videoBox;
//
//    //皮肤是否一直黑色，暂时skin14是，用来控制字母颜色都为黑色
//    boolean isSkinAlwaysBlack;
//    boolean useKuaiB;
//    String skinName;
//
//    private int beeState;
//    private SpineGroup[] bees;
//    private Position[] beePositions;
//    private String maxLengthWord;
//    private int beeDisappearCount;
//    private int levelBees;
//    private int maxBees;
//
//    public GameplayScreen(MyGame game) {
//        super(game, "GameplayScreen");
//        gameData = GameData.instance;
//        gameMode = gameData.gameMode;
//        Assets.getInstance().loadGameplayAssets();
//        Assets.getInstance().assetManager.finishLoading();
//        GameplayAssets.load();
//        managerUIEditor = Assets.getInstance().getAssetManager().get(Constants.gameplayUIPath);
//        baseGroup = managerUIEditor.createGroup();
//        baseGroup.setTransform(false);
//        gameplayButtonUIEditor = Assets.getInstance().getAssetManager().get(Constants.gameplayButtonUIPath);
//    }
//
//
//    @Override
//    public void show() {
//        super.show();
//        RuntimeData.instance.dailyRewardCoin = 0;
//        gameData.canClickNextGame = true;
//        isJiesuan = false;
//        isYindaoWithoutState = false;
//        isWordYindao = false;
//        gameData.DDNATimeCount = 0;
//        gameData.DDNAGameHasReward = false;
//        gameData.errorGameTime = 0;
//
//        gameData.canOutputMayFeelHard = true;
//        gameData.changeMayFeelHardSaveIndex = (gameData.changeMayFeelHardSaveIndex + 1) % 20;
//
//
//        initDDNAData();
//
//        gameData.useKuaiB = gameData.getKuaiB();
//        useKuaiB = gameData.useKuaiB;
//
//        gameData.skinName = gameData.getSkinName();
//        skinName = gameData.skinName;
//        if (skinName.equals("skin14")) {
//            isSkinAlwaysBlack = true;
//        } else if (gameData.nowBgName.equals("120")) {
//            isSkinAlwaysBlack = true;
//        } else {
//            isSkinAlwaysBlack = false;
//        }
//        //初始化每关找到的单词和额外单词
//        gameData.findWordIndex = -1;
//        gameData.findExtraWordIndex = -1;
//        gameData.findWordLength = 0;
//        gameData.findExtraWordLength = 0;
//
//
//        fasthintArr = new Position[Constants.MAXWORDNUM];
//        for (int i = 0; i < Constants.MAXWORDNUM; i++) {
//            fasthintArr[i] = new Position();
//        }
//        fasthintWordArr = new Position[7];
//        for (int i = 0; i < 7; i++) {
//            fasthintWordArr[i] = new Position();
//        }
//
//        yindaoWordIndex = -1;
//
//        emptyBox = new BoxUtils<>();
//
//
//        if (gameMode == MyEnum.GameMode.normalMode) {
//            RuntimeData.instance.levelStartCount++;
//            initData();
//            if(gameData.chapterIs<ChapterUtil.chapters.size) {
//                ChapterUtil.ChapterData chapterData = ChapterUtil.chapters.get(gameData.chapterIs + 1);
//                title = chapterData.title;
//                levelText = title + " - " + levelIs + " (" + (gameData.gameIs + 1) + ")";
//            }else{
//                String randomTitle = GameData.instance.getRandomTitle();
//                if(randomTitle == null || randomTitle.equals("")){
//                    GameData.instance.setRandomBg(GameData.instance.chapterIs);
//                    randomTitle = GameData.instance.getRandomTitle();
//                }
//                title = randomTitle;
//                levelText = randomTitle + " - " + levelIs + " (" + (gameData.gameIs + 1) + ")";
//            }
//        }else {
//            initDailyData();
//            levelText = Constants.MONTH_TEXT[CalendarUtils.getMonth(gameData.selectDay)].substring(0, 3) + ". " + CalendarUtils.getDay(gameData.selectDay);
//        }
//
//        gameData.DDNAErrorCount = 0;
//        gameData.DDNABonusCount = 0;
//        gameData.levelIs = levelIs;
//
//
//        initStage();
//        inputMultiplexer.addProcessor(stage);
//
//        PanelLoad();
//        ButtonLoad();
//
//
//        setInputProcessor(false);
//
//        if(GameData.instance.newBgshowChapterInfo && GameData.instance.bigChapterIs<4){
//            GameData.instance.newBgshowChapterInfo = false;
//            Group chapterInfo = Assets.getInstance().assetManager.get(Constants.chpaterSwitchPath,ManagerUIEditor.class).createGroup();
//            int bgIndex = bgGroup[game.getNowBgUseIndex()].getZIndex();
//            bgGroup[game.getNowBgUseIndex()].toFront();
//            stage.addActor(chapterInfo);
//            for(int i = 1;i<=4;i++){
//                chapterInfo.findActor(i+"").setVisible(false);
//            }
//            chapterInfo.setY(-130);
//            chapterInfo.findActor(""+(GameData.instance.bigChapterIs+1)).setVisible(true);
//            Actor[] actors = new Actor[3];
//            actors[0] = chapterInfo.findActor("bit_title");
//            actors[1] = chapterInfo.findActor("chapter_name");
//            ((Label)actors[1]).setText(title);
//            actors[2] = chapterInfo.findActor("levelInfo");
//
//            int levelStart = ChapterUtil.getChapterFirstLevel(GameData.instance.chapterIs)+1;
//            int levelEnd = levelStart+15;
//            if(GameData.instance.chapterSolved<ChapterUtil.chapterCount){
//                levelEnd = ChapterUtil.chapters.get(GameData.instance.chapterSolved+1).levelEnd + 1;
//            }
//            ((Label)actors[2]).setText("Level " + levelStart + "-"+levelEnd);
//            for(int i = 0;i<3;i++){
//                actors[i].getColor().a = 0;
//                actors[i].addAction(Actions.delay(i*0.3F,Actions.parallel(Actions.alpha(1,0.3F),Actions.moveBy(0,130,0.3F))));
//            }
//            chapterInfo.addAction(Actions.sequence(Actions.delay(1.5F,Actions.run(()->{
//                for(int i = 0;i<3;i++){
//                    actors[i].addAction(Actions.delay((2-i)*0.2F,Actions.parallel(Actions.alpha(0,0.2F),Actions.moveBy(0,-100,0.2F))));
//                }
//            })),Actions.delay(1.2F,Actions.run(()->{
//                showAnimation();
//                Gdx.app.postRunnable(()->{
//                    bgGroup[game.getNowBgUseIndex()].setZIndex(bgIndex);
//                });
//            })),Actions.removeActor()));
//        }else {
//            showAnimation();
//        }
//        if(debug) {
//            for (int i = 0; i < info.length; i++) {
//                info[i] = baseGroup.findActor("info" + i);
//            }
//            baseGroup.findActor("info").setTouchable(Touchable.disabled);
//        }else{
//            Actor infos = baseGroup.findActor("info");
//            if(infos!=null){
//                infos.remove();
//            }
//        }
//
//        if(maxLengthWord!=null && beePositions!=null){
//            maxBees = maxLengthWord.length();
//        }
//    }
//
//    private Label[] info = new Label[8];
//
//    private void showAnimation(){
//        setAppearAction();
//        showQuestWarningGroup("questUnlockBgPicturesNum", 0);
//
//
//        gameData.flurryBgPictureTime++;
//        if (gameData.flurryBgPictureTime == 5 && !gameData.flurryBgPictureName.equals("")) {
//            PlatformManager.instance.outPut("Challenge", "Bgpicture", gameData.flurryBgPictureName);
//        }
//
//
//        unloadDoNotNeedBg();
//        if (!gameData.propPanelUserType.equals("")) {
//            gameData.propNotShowLevelTime++;
//            Prefs.putInteger("propNotShowLevelTime", gameData.propNotShowLevelTime);
//            Prefs.flush();
//        }
//
//        showLoadBgAnimation();
//        startTime = System.currentTimeMillis();
//    }
//
//
//
//    float ddnaOperationIntervalTimeQ1, ddnaOperationIntervalTimeQ3, ddnaOperationIntervalTimeCompareValue = -1;
//    float ddnaOperationInvalidWordQ1, ddnaOperationInvalidWordQ3, ddnaOperationInvalidWordCompareValue = -1;
//    String levelType;
//
//    //初始化ddna部分数据
//    void initDDNAData() {
//        if (gameMode == MyEnum.GameMode.normalMode) {
//            levelType = "normal";
//        } else {
//            levelType = "challenge";
//        }
//        gameData.DDNALevelType = levelType;
//
//        gameData.DDNAOperationIntervalTime = 0;
//        gameData.DDNAOperationIntervalSaveTime = new StringBuilder();
//        Array<Float> DDNAOperationIntervalTimeArray = new Array<>();
//        for (int i = 0; i < 20; i++) {
//            String prefsName = "DDNAOperationIntervalSaveTime" + i;
//            if (Prefs.containsKey(prefsName)) {
//                String temp[] = Prefs.getString(prefsName).split(",");
//
//                for (int k = 0; k < temp.length; k++) {
//                    float v = Float.parseFloat(temp[k]);
//                    DDNAOperationIntervalTimeArray.add(v);
//                }
//            }
//        }
//
//        if (DDNAOperationIntervalTimeArray.size >= 4) {
//            //排序，从小到大
//            DDNAOperationIntervalTimeArray.sort();
//            int len = DDNAOperationIntervalTimeArray.size;
//            ddnaOperationIntervalTimeQ1 = DDNAOperationIntervalTimeArray.get(len / 4);
//            ddnaOperationIntervalTimeQ3 = DDNAOperationIntervalTimeArray.get(len * 3 / 4);
//            ddnaOperationIntervalTimeCompareValue = ddnaOperationIntervalTimeQ3 + 3 * (ddnaOperationIntervalTimeQ3 - ddnaOperationIntervalTimeQ1) / 2;
//        }
//
//
//        gameData.DDNAOperationInvalidWord = 0;
//        gameData.DDNAOperationInvalidSaveWord = new StringBuilder();
//        Array<Integer> DDNAOperationInvalidSaveWordArray = new Array<>();
//        for (int i = 0; i < 20; i++) {
//            String prefsName = "DDNAOperationInvalidSaveWord" + i;
//            if (Prefs.containsKey(prefsName)) {
//                String temp[] = Prefs.getString(prefsName).split(",");
//
//                for (int k = 0; k < temp.length; k++) {
//                    int v = Integer.parseInt(temp[k]);
//                    DDNAOperationInvalidSaveWordArray.add(v);
//                }
//            }
//        }
//
//        if (DDNAOperationInvalidSaveWordArray.size >= 4) {
//            //排序，从小到大
//            DDNAOperationInvalidSaveWordArray.sort();
//            int len = DDNAOperationInvalidSaveWordArray.size;
//            ddnaOperationInvalidWordQ1 = DDNAOperationInvalidSaveWordArray.get(len / 4);
//            ddnaOperationInvalidWordQ3 = DDNAOperationInvalidSaveWordArray.get(len * 3 / 4);
//            ddnaOperationInvalidWordCompareValue = ddnaOperationInvalidWordQ1 + 3 * (ddnaOperationInvalidWordQ3 - ddnaOperationInvalidWordQ1) / 2;
//        }
//
//
//    }
//
//
//    void initData() {
//
//        if (gameData.trieTree == null) {
//            Assets.getInstance().assetManager.load(Constants.allDataPath, Text.class);
//            Assets.getInstance().assetManager.finishLoading();
////				gongyongAssetsPath.add(Constants.allDataPath);
//            gameData.trieTree = new TrieTree(Assets.getInstance().assetManager.get(Constants.allDataPath, Text.class).getString());
//            Assets.getInstance().assetManager.unload(Constants.allDataPath);
//        }
//
//
//        stateStr = "state";
//        hintAppearStr = "hintAppear_";
//        hintIndexStr = "hintIndex";
//        extraStateStr = "extraState";
//        isFinger = false;
//        gameIs = gameData.gameIs;
//        gameSolved = gameData.gameSolved;
//
//
//        int tempGameIs = gameData.gameIs;
//        if (gameIs >= Constants.MAX_GAMELEVEL) {
//            tempGameIs = gameIs - Constants.MAX_GAMELEVEL + 4000;
//        }
//
//
//        if (gameSolved == gameIs) {
//            gameData.isNewLevel = true;
//            isNewLevel = true;
//            GameData.instance.levelCost = 0;
//            GameData.instance.levelFeelHarCount = 0;
//        } else {
//            gameData.isNewLevel = false;
//            isNewLevel = false;
//        }
//
//
//        //载入对应关卡数据
//        if (!gameData.isOldVersion && gameData.bVersionDataTreeMap != null && gameData.bVersionDataTreeMap.containsKey(tempGameIs)) {
//            useDNALevel = true;
//            DDNALeverVersion = gameData.DNADataVersion;
//        }
//        gameData.levelVersion = DDNALeverVersion;
//
//
//        if (isNewLevel) {
//            //新的关卡使用其他数据更新关卡数据
//            if (useDNALevel) {
////				System.out.println(Prefs.getString("nowLevelDataVersion", "LocalDataVersion"));
////				System.out.println(gameData.DNADataVersion);
//                if (!gameData.nowLevelDataVersion.equals(gameData.DNADataVersion)) {
//                    //使用dna关卡时候如果使用数据不是当前dna数据
//                    Prefs.instance.updateNewDataVersion();
//                    Prefs.putString("nowLevelDataVersion", gameData.DNADataVersion);
//                    Prefs.flush();
//                    gameData.nowLevelDataVersion = gameData.DNADataVersion;
////					PlatformManager.instance.showLog("updatData");
//                }
//
//            } else {
//                //使用本地关卡的时候如果使用数据不是本地数据
//                if (!Prefs.getString("nowLevelDataVersion", "LocalDataVersion").equals("LocalDataVersion")) {
//                    Prefs.instance.updateNewDataVersion();
//                    Prefs.putString("nowLevelDataVersion", "LocalDataVersion");
//                    Prefs.flush();
//                    gameData.nowLevelDataVersion = "local";
//                }
//            }
//
//            gameData.DDNAStartRecord++;
//            Prefs.putInteger("DDNAStartRecord", gameData.DDNAStartRecord);
//            gameData.DDNALevelText = (gameData.gameIs + 1) + "";
//            PlatformManager.instance.levelStart("normal", gameData.DDNALevelText, gameData.coinNumber, gameData.allFengNum, gameData.DDNAStartRecord, DDNALeverVersion);
//        } else {
//            gameData.DDNALevelText = (gameData.gameIs + 1) + "";
//            PlatformManager.instance.levelStart("normal", gameData.DDNALevelText, gameData.coinNumber, gameData.allFengNum, 1, DDNALeverVersion);
//        }
//
//
//        //设置在第几章的第几关
//        levelIs = gameData.gameIs - ChapterUtil.getChapterFirstLevel(gameData.chapterIs) + 1;
//
//        PlatformManager.instance.outPut("Enter", "level", (gameData.gameIs + 1) + "", true);
//
//
//        if (isNewLevel) {
//            levelBees = Prefs.getInteger("levelBees");
//            wordState = Prefs.getInteger(stateStr, 0);
//            beeState = Prefs.getInteger("beeState");
//        } else {
//            levelBees = 0;
//            wordState = 0;
//            beeState = 0;
//        }
//        skinState = wordState;
//
//
//        String temp;
//        String string;
//        String splitString[];
//        //载入对应关卡数据
//        if (useDNALevel) {
//            string = gameData.bVersionDataTreeMap.get(tempGameIs);
//            splitString = string.split(";");
//        } else {
//            String fileName;
//            //载入对应关卡数据
//            int fileIndex = tempGameIs / 10;
//            fileName = "gameplay/data/data" + fileIndex + ".txt";
//            if (!Assets.getInstance().assetManager.isLoaded(fileName)) {
//                Assets.getInstance().assetManager.load(fileName, Text.class);
//                Assets.getInstance().assetManager.finishLoading();
//            }
//
//
//            string = Assets.getInstance().assetManager.get(fileName, Text.class).getString();
//            String[] middleStr = string.split("\\n");
//            splitString = middleStr[tempGameIs % 10].split(";");
//        }
//
//
//        temp = splitString[0];
//        coinWord = splitString[1];
//
//
//        //获得关卡的整个数据，对这个拆分可以获得具体数据
//        words = temp.split(",");
//
//
//        PlatformManager.instance.showLog("gameIs-----" + gameIs);
//
//
//        wordCount = (words.length - 4) / 5;
//        w = Integer.parseInt(words[2]) + 1;
//        h = Integer.parseInt(words[3]) + 1;
//        gridMsg = new GridMsg[h][w];
//        wordMsg = new WordMsg[wordCount];
//
//        passWordState = (1 << wordCount) - 1;
//        if(wordState == passWordState){          //解决老用户卡死问题
//            clearCurrentData();
//        }
//
//        gameLetter = words[1];
//
//        letterLength = gameLetter.length();
//
//        gameAnswer = new String[wordCount];
//
//        wordUsed = new boolean[wordCount];
//        for (int i = 0; i < wordCount; i++) {
//            gameAnswer[i] = words[4 + i * 5];
//            if(maxLengthWord == null || gameAnswer[i].length()>maxLengthWord.length()){
//                maxLengthWord = gameAnswer[i];
//            }
//            wordMsg[i] = new WordMsg();
//            wordMsg[i].str = gameAnswer[i];
//            wordMsg[i].x = Integer.parseInt(words[4 + i * 5 + 1]);
//            wordMsg[i].y = Integer.parseInt(words[4 + i * 5 + 2]);
//            wordMsg[i].length = Integer.parseInt(words[4 + i * 5 + 3]);
//            wordMsg[i].dir = MyEnum.getDir(Integer.parseInt(words[4 + i * 5 + 4]));
//        }
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++)
//                gridMsg[i][j] = new GridMsg();
//        }
//
//        for (int k1 = 0; k1 < h; k1++) {
//            for (int k2 = 0; k2 < w; k2++) {
//                if (isNewLevel) {
//                    gameData.hintAppear[k1][k2] = Prefs.getBoolean(hintAppearStr + k1 + "_" + k2, false);
//                } else {
//                    gameData.hintAppear[k1][k2] = false;
//                }
//
//            }
//        }
//
//
//        for (int i = 0; i < wordCount; i++) {
//            if (isNewLevel) {
//                gameData.hintIndex[i] = Prefs.getInteger(hintIndexStr + i, 0);
//                if (gameData.hintIndex[i] > gameAnswer[i].length()) {
//                    gameData.hintIndex[i] = gameAnswer[i].length();
//                }
//            } else {
//                gameData.hintIndex[i] = 0;
//            }
//        }
//
//        gameData.extraWordCount = Prefs.getInteger("extraWordCount", 0);
//
//        StringBuilder sb = new StringBuilder();
//        extraWord = gameData.trieTree.getAllword(gameLetter);
//        for (int i = 0; i < extraWord.length; i++) {
//            sb.append("0");
//        }
//        for (int i = 0; i < extraWord.length; i++) {
//            for (int j = 0; j < wordCount; j++) {
//                if (extraWord[i].equals(gameAnswer[j]) || (coinWord != "NULL" && extraWord[i].equals(coinWord))) {
//                    //金币单词之类用2代替，而不是1
//                    sb.replace(i, i + 1, "2");
//                }
//            }
//        }
//
//
//        String tempSb = sb.toString();
//        if(isNewLevel){
//            gameData.extraState = new StringBuilder(Prefs.getString(extraStateStr, tempSb));
//        }else{
//            gameData.extraState = sb;
//        }
//
//        if (gameData.extraState.length() != tempSb.length()) {
//            //更新数据后的问题
//            gameData.extraState = new StringBuilder(tempSb);
//        }
//
//
//        //将额外单词添加入词典
//        for (int i = 0; i < extraWord.length; i++) {
//            if (gameData.extraState.charAt(i) == '1') {
////				extraWordPanel.addWord(extraWord[i]);
//                gameData.findWord[gameData.findWordLength] = extraWord[i];
//                gameData.findWordLength++;
//                gameData.findExtraWord[gameData.findExtraWordLength] = extraWord[i];
//                gameData.findExtraWordLength++;
//            }
//            gameData.findWordShowAudio[i] = false;
//            gameData.findExtraWordShowAudio[i] = false;
//        }
//
//
//        //闪退
//        fengPosition = new Position(-1, -1, -1, -1);
//
//
//    }
//
//    private void clearCurrentData(){
//        for (int i = 0; i < Constants.MAXWORDNUM; i++) {
//            Prefs.putInteger(hintIndexStr + i, 0);
//        }
//        for (int i = 0; i < Constants.MAXWORDNUM; i++) {
//            for (int j = 0; j < Constants.MAXWORDNUM; j++) {
//                Prefs.putBoolean(hintAppearStr + i + "_" + j, false);
//            }
//        }
//
//        Prefs.flush();
//        wordState = 0;
//        skinState = 0;
//        beeState = 0;
//        levelBees = 0;
//    }
//
//    void initDailyData() {
//        gameData.dailyPass = false;
//        if (gameData.trieTree == null) {
//            Assets.getInstance().assetManager.load(Constants.allDataPath, Text.class);
//            Assets.getInstance().assetManager.finishLoading();
//            gameData.trieTree = new TrieTree(Assets.getInstance().assetManager.get(Constants.allDataPath, Text.class).getString());
//            Assets.getInstance().assetManager.unload(Constants.allDataPath);
//
//        }
//
//        stateStr = "dailyState";
//        hintAppearStr = "dailyHintAppear_";
//        hintIndexStr = "dailyHintIndex";
//        extraStateStr = "dailyExtraState";
//        isNewLevel = true;
//
//        boolean isNewDay = false;
//        int dataDay = Prefs.getInteger("dailyData_day",-1);
//
//        //当是新的一天的时候
//        if (Prefs.getBoolean("updateDaily", false) || Prefs.getBoolean("updateDailyData", false) || dataDay!=gameData.selectDay) {
//
//            Prefs.putBoolean("updateDailyData", false);
//            Prefs.putBoolean("updateDaily", false);
//            Prefs.putInteger(stateStr, 0);
//            Prefs.putInteger("dailyData_day",gameData.selectDay);
//            Prefs.putInteger("dailyJindu", 0);
//            Prefs.putString("dailyPosition", GameData.json.prettyPrint(new Position(-1, -1, -1, -1)));
//            clearCurrentData();
//            isNewDay = true;
//            gameData.dayStarNum = 0;
//        }
//
//        isFinger = false;
//        wordState = Prefs.getInteger(stateStr, 0);
//        skinState = wordState;
//
//        gameData.DDNADailyStartRecord++;
//        String temp;
////        Prefs.getInteger("dailyUnlockDay",0)
//        boolean hasEnterDaily = Prefs.getBoolean("hasEnterDaily", false);
//        int dailyFixedLevelDay = Prefs.getInteger("dailyFixedLevel_day",-1);
//        if (!hasEnterDaily || GameData.instance.selectDay == dailyFixedLevelDay) {
//            if(dailyFixedLevelDay == -1){
//                Prefs.putInteger("dailyFixedLevel_day",gameData.selectDay);
//            }
//            Prefs.putBoolean("hasEnterDaily", true);
//            temp = "0,AYERL,7,6,RELAY,0,0,5,0,EAR,0,1,3,1,YEAR,0,4,4,1,EARL,1,4,4,0,LAY,1,7,3,1,ARE,2,0,3,0,EARLY,2,2,5,1,RELY,3,4,4,0,LYRE,3,6,4,1,LAYER,5,2,5,0;NULL";
//            gameData.DDNALevelText = -1 + "";
//            PlatformManager.instance.levelStart("challenge", gameData.DDNALevelText, gameData.coinNumber, gameData.allFengNum, gameData.DDNADailyStartRecord, DDNALeverVersion);
//        } else {
//            int t = CalendarUtils.getDayGap(CalendarUtils.getYear(gameData.selectDay), CalendarUtils.getMonth(gameData.selectDay), CalendarUtils.getDay(gameData.selectDay)) % WordData.dailyCount + 1;
//            gameData.DDNALevelText = t + "";
//            PlatformManager.instance.levelStart("challenge", gameData.DDNALevelText, gameData.coinNumber, gameData.allFengNum, gameData.DDNADailyStartRecord, DDNALeverVersion);
////			PlatformManager.instance.showLog("第" + (CalendarUtils.getDayGap() % WordData.dailyCount+1) + "个");
//
//            //载入对应关卡数据
//            int fileIndex = Math.min(53, t / 10);
//            String fileName = "gameplay/dailydata/dailydata" + fileIndex + ".txt";
//            if (!Assets.getInstance().assetManager.isLoaded(fileName)) {
//                Assets.getInstance().assetManager.load(fileName, Text.class);
//                Assets.getInstance().assetManager.finishLoading();
//            }
//
//            String string = Assets.getInstance().assetManager.get(fileName, Text.class).getString();
//            String[] middleStr = string.split("\\n");
//            temp = middleStr[t % 10];
//        }
//
//        temp = temp.split(";")[0];
//        words = temp.split(",");
//
//
//        wordCount = (words.length - 4) / 5;
//        w = Integer.parseInt(words[2]) + 1;
//        h = Integer.parseInt(words[3]) + 1;
//        gridMsg = new GridMsg[h][w];
//        wordMsg = new WordMsg[wordCount];
//        passWordState = (1 << wordCount) - 1;
//
//        gameLetter = words[1];
//
//        letterLength = gameLetter.length();
//
//        gameAnswer = new String[wordCount];
//        wordUsed = new boolean[wordCount];
//        for (int i = 0; i < wordCount; i++) {
//            gameAnswer[i] = words[4 + i * 5];
//            wordMsg[i] = new WordMsg();
//            wordMsg[i].str = gameAnswer[i];
//            wordMsg[i].x = Integer.parseInt(words[4 + i * 5 + 1]);
//            wordMsg[i].y = Integer.parseInt(words[4 + i * 5 + 2]);
//            wordMsg[i].length = Integer.parseInt(words[4 + i * 5 + 3]);
//            wordMsg[i].dir = MyEnum.getDir(Integer.parseInt(words[4 + i * 5 + 4]));
//        }
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++)
//                gridMsg[i][j] = new GridMsg();
//        }
//
//        for (int k1 = 0; k1 < h; k1++) {
//            for (int k2 = 0; k2 < w; k2++) {
//                gameData.hintAppear[k1][k2] = Prefs.getBoolean(hintAppearStr + k1 + "_" + k2, false);
//            }
//        }
//
//
//        for (int i = 0; i < wordCount; i++) {
//            if (isNewLevel) {
//                gameData.hintIndex[i] = Prefs.getInteger(hintIndexStr + i, 0);
//                if (gameData.hintIndex[i] > gameAnswer[i].length()) {
//                    gameData.hintIndex[i] = gameAnswer[i].length();
//                }
//            } else {
//                gameData.hintIndex[i] = 0;
//            }
//        }
//
//        coinWord = "NULL";
//
//
//        gameData.extraWordCount = Prefs.getInteger("extraWordCount", 0);
//        extraWord = gameData.trieTree.getAllword(gameLetter);
//        if (isNewDay) {
//            StringBuilder sb = new StringBuilder();
//
//            for (int i = 0; i < extraWord.length; i++) {
//                sb.append("0");
//            }
//            for (int i = 0; i < extraWord.length; i++) {
//                for (int j = 0; j < wordCount; j++) {
//                    if (extraWord[i].equals(gameAnswer[j]) || (coinWord != "NULL" && extraWord[i].equals(coinWord))) {
//                        //金币单词之类用2代替，而不是1
//                        sb.replace(i, i + 1, "2");
//                    }
//                }
//            }
//            gameData.extraState = sb;
//        } else {
//            gameData.extraState = new StringBuilder(Prefs.getString(extraStateStr));
//
//            int length = extraWord.length;
//
//            if (gameData.extraState.length() != length) {
//                StringBuilder sb = new StringBuilder();
//
//                for (int i = 0; i < extraWord.length; i++) {
//                    sb.append("0");
//                }
//
//                for (int i = 0; i < extraWord.length; i++) {
//                    for (int j = 0; j < wordCount; j++) {
//                        if (extraWord[i].equals(gameAnswer[j]) || (coinWord != "NULL" && extraWord[i].equals(coinWord))) {
//                            //金币单词之类用2代替，而不是1
//                            sb.replace(i, i + 1, "2");
//                        }
//                    }
//                }
//                //更新数据后的问题
//                gameData.extraState = sb;
//            }
//        }
//
//
//        //将额外单词添加入词典
//        for (int i = 0; i < extraWord.length; i++) {
//            if (gameData.extraState.charAt(i) == '1') {
////				extraWordPanel.addWord(extraWord[i]);
//                gameData.findWord[gameData.findWordLength] = extraWord[i];
//                gameData.findWordLength++;
//                gameData.findExtraWord[gameData.findExtraWordLength] = extraWord[i];
//                gameData.findExtraWordLength++;
//            }
//            gameData.findWordShowAudio[i] = false;
//            gameData.findExtraWordShowAudio[i] = false;
//        }
//
//
//        dailyJindu = Prefs.getInteger("dailyJindu", 0);
//        gameData.dailyJindu = dailyJindu;
//        String p = Prefs.getString("dailyPosition", null);
//        if (p != null) {
//            fengPosition = GameData.json.fromJson(Position.class, p);
//        } else {
//            fengPosition = new Position(-1, -1, -1, -1);
//        }
//
//    }
//
//    int[] letterIndexBuffer;
//    int letterIndex;
//
//    StringBuilder stringBuilder = new StringBuilder();
//    int findLetterX, findLetterY;
//
//    boolean isLianxian = false;
//    //用于控制拉伸条的action
//    Actor actionActor;
//
//    void initStage() {
//
//
//        stage = new Stage(game.getExtendViewport(), game.getPolygonSpriteBatch());
//        stage.getCamera().position.set(360, 640, 0);
//        stage.getRoot().setName("stageGroup");
//
//        if (gameData.changeBgEntrance.equals("postcard")) {
//            addPostcardBgActor();
//        } else {
//            addBgActor();
//        }
//
//
//        loadTouchParticle();
//        initBgSwitchGroup();
//        actionActor = new Actor();
//        stage.addActor(actionActor);
//
//        stage.addActor(baseGroup);
//
//        mask = new Image(new NinePatch(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/mask"), 1, 1, 1, 1));
//        mask.setColor(mask.getColor().r, mask.getColor().g, mask.getColor().b, 0f);
//        mask.setSize(ViewportUtils.getWidth(), ViewportUtils.getHeight());
//        mask.setPosition(ViewportUtils.getLeft(), ViewportUtils.getBottom());
//        stage.addActor(mask);
//        mask.setVisible(false);
//
//        Vector2 bottomLetterPos = new Vector2(0, 0);
//
//        inputListener = new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                // TODO Auto-generated method stub
//                setTouch(x, y);
//                if (mask != null && mask.isVisible() && !canIgnoreMask) {
//                    return false;
//                }
//
//                if (canLianxian) {
//                    letterIndex = 0;
//                    preview.reset();
//                    preview.toFront();
//
//
//                    stringBuilder.delete(0, stringBuilder.length());
//
//                    //finger
//                    findLetterX = -1;
//                    findLetterY = -1;
//                    if (isFinger) {
//                        for (int i = 0; i < h; i++) {
//                            for (int j = 0; j < w; j++) {
//
//                                if (letter[i][j] != null && letter[i][j].getX() <= x && x <= letter[i][j].getX() + geziSizeX &&
//                                        letter[i][j].getY() <= y && y <= letter[i][j].getY() + geziSizeY) {
//                                    findLetterX = i;
//                                    findLetterY = j;
//                                    break;
//                                }
//                            }
//                        }
//                    } else {
//                        for (int i = 0; i < letterLength; i++) {
//                            bottomLetterPos.set(0, 0);
//                            bottomLettersGroup[i].localToStageCoordinates(bottomLetterPos);
//                            if ((Math.abs(bottomLetterPos.x + bottomLettersGroup[i].getWidth() / 2f - x) <= bottomLettersGroup[i].getWidth() / 2f) &&
//                                    (Math.abs(bottomLetterPos.y + bottomLettersGroup[i].getHeight() / 2f - y) <= bottomLettersGroup[i].getHeight() / 2f)) {
//                                for (int j = 0; j < yindaoXuxianCnt; j++) {
//                                    yindaoXuxian[j].setVisible(false);
//                                }
//                                if (yindaoXuxianCnt != 0) {
//                                    yindaoShou.setVisible(false);
//                                }
//                                if (!isLinked[i]) {
//                                    gameData.DDNALianciTime = 0;
//                                    isLianxian = true;
//                                    woodImage[i].setVisible(true);
//                                    if (useWhiterPin && !isSkinAlwaysBlack) {
//                                        bottomLetterLbl[i].setColor(Color.WHITE);
//                                    }
//
//                                    letterIndexBuffer[letterIndex++] = i;
//                                    isLinked[i] = true;
//                                    myLine.addPoint(bottomLettersGroup[i].getX() + bottomLettersGroup[i].getWidth() / 2f, bottomLettersGroup[i].getY() + bottomLettersGroup[i].getHeight() / 2f);
//
//                                    stringBuilder.append(gameLetter.charAt(i));
//                                    preview.setText(stringBuilder.toString());
//
//                                } else if (isLinked[i] && letterIndex >= 2 && (letterIndexBuffer[letterIndex - 2] == i)) {
//                                    woodImage[letterIndexBuffer[letterIndex - 1]].setVisible(false);
//                                    if (useWhiterPin) {
//                                        bottomLetterLbl[letterIndexBuffer[letterIndex - 1]].setColor(Color.BLACK);
//                                    }
//
////
//                                    letterIndex--;
//                                    isLinked[letterIndexBuffer[letterIndex]] = false;
//                                    myLine.popPoint();
//                                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//                                    preview.setText(stringBuilder.toString());
//                                }
//                                break;
//                            }
//                        }
//                        myLine.setLastPoint(x - pinGroup.getX(), y - pinGroup.getY());
//                    }
//                }
//                return true;
//            }
//
//            @Override
//            public void touchDragged(InputEvent event, float x, float y, int pointer) {
//
//                //因为现在商店面板没遮罩了
//                if (mask != null && mask.isVisible() && !canIgnoreMask || hasPanelShowing() || !canLianxian) {
//                    return;
//                }
//                if (isFinger) {
//                    super.touchDragged(event, x, y, pointer);
//                    return;
//                }
//
//
//                for (int i = 0; i < letterLength; i++) {
//                    bottomLetterPos.set(0, 0);
//                    bottomLettersGroup[i].localToStageCoordinates(bottomLetterPos);
//                    if ((Math.abs(bottomLetterPos.x + bottomLettersGroup[i].getWidth() / 2f - x) <= bottomLettersGroup[i].getWidth() / 2f * touchScaleX) &&
//                            (Math.abs(bottomLetterPos.y + bottomLettersGroup[i].getHeight() / 2f - y) <= bottomLettersGroup[i].getHeight() / 2f * touchScaleY)) {
//                        for (int j = 0; j < yindaoXuxianCnt; j++) {
//                            yindaoXuxian[j].setVisible(false);
//                        }
//                        if (yindaoXuxianCnt != 0) {
//                            yindaoShou.setVisible(false);
//                        }
//                        if (!isLinked[i]) {
//
//                            if (letterIndex == 0) {
//                                isLianxian = true;
//                                gameData.DDNALianciTime = 0;
//                            }
//
//
//                            woodImage[i].setVisible(true);
////							woodImage[i].setColor(new Color(241f/255f, 157f/255f, 29f/255f, 1));
//                            if (useWhiterPin && !isSkinAlwaysBlack) {
////								bottomLetterLbl[i].setColor(65 / 255f, 33 / 255f, 22 / 255f, 1);
//                                bottomLetterLbl[i].setColor(Color.WHITE);
//                            }
//
////							bottomLettersGroup[i].addAction(Actions.scaleTo(1.05f, 1.05f, 0.2f));
//                            letterIndexBuffer[letterIndex++] = i;
//                            isLinked[i] = true;
//
//                            AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_TAP_PATH[Math.min(letterLength - 1, myLine.getPointTotal())], Sound.class), 0.2f);
//                            myLine.addPoint(bottomLettersGroup[i].getX() + bottomLettersGroup[i].getWidth() / 2f, bottomLettersGroup[i].getY() + bottomLettersGroup[i].getHeight() / 2f);
//
//                            stringBuilder.append(gameLetter.charAt(i));
//                            preview.setText(stringBuilder.toString());
//
//                        } else if (isLinked[i] && letterIndex >= 2 && (letterIndexBuffer[letterIndex - 2] == i)) {
//                            woodImage[letterIndexBuffer[letterIndex - 1]].setVisible(false);
//                            if (useWhiterPin) {
//                                bottomLetterLbl[letterIndexBuffer[letterIndex - 1]].setColor(Color.BLACK);
//                            }
//
////							AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_TAP_PATH[Math.min(letterIndex, myLine.getPointTotal())], Sound.class), 0.2f);
//                            letterIndex--;
//                            isLinked[letterIndexBuffer[letterIndex]] = false;
//                            AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_TAP_PATH[Math.max(0, Math.min(letterIndex - 1, myLine.getPointTotal()))], Sound.class), 0.2f);
//                            myLine.popPoint();
//                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//                            preview.setText(stringBuilder.toString());
//                        }
//                        break;
//                    }
//                }
//                myLine.setLastPoint(x - pinGroup.getX(), y - pinGroup.getY());
//                super.touchDragged(event, x, y, pointer);
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                // TODO Auto-generated method stub
//                super.touchUp(event, x, y, pointer, button);
//
//
//                //引导虚线和引导手的设置
//                if (yindaoXuxianCnt != 0) {
//                    setYindaoActions();
//                }
//                if (isFinger && findLetterX >= 0 && findLetterY >= 0 && !(findLetterX==videoBoxPos.x && findLetterY==videoBoxPos.y)) {
//                    MyEnum.DDNAItemUseState ddnaItemUseState = fingerhint();
//
//                    if (gameMode == MyEnum.GameMode.normalMode) {
//                        if (ddnaItemUseState == MyEnum.DDNAItemUseState.useFree || ddnaItemUseState == MyEnum.DDNAItemUseState.useExistingItem) {
//                            if (gameData.dnaSetPropPanelMoreFlag) {
//                                gameData.dnaSetPropPanelNowM = 0;
//                                Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                            }
//                            PlatformManager.instance.itemActioned("Item2", 0, gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                        } else if (ddnaItemUseState == MyEnum.DDNAItemUseState.useCoin) {
//                            if (gameData.dnaSetPropPanelMoreFlag) {
//                                gameData.dnaSetPropPanelNowM = 0;
//                                Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                            }
//                            PlatformManager.instance.itemActioned("Item2", fingerBtn.getCoinValue(), gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                        }
//                    } else {
//                        if (ddnaItemUseState == MyEnum.DDNAItemUseState.useFree || ddnaItemUseState == MyEnum.DDNAItemUseState.useExistingItem) {
//                            if (gameData.dnaSetPropPanelMoreFlag) {
//                                gameData.dnaSetPropPanelNowM = 0;
//                                Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                            }
//                            PlatformManager.instance.itemActioned("Item2", 0, gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                        } else if (ddnaItemUseState == MyEnum.DDNAItemUseState.useCoin) {
//                            if (gameData.dnaSetPropPanelMoreFlag) {
//                                gameData.dnaSetPropPanelNowM = 0;
//                                Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                            }
//                            PlatformManager.instance.itemActioned("Item2", fingerBtn.getCoinValue(), gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                        }
//                    }
//
//                    return;
//                }
//                for (int i = 0; i < letterLength; i++) {
//                    isLinked[i] = false;
//                    woodImage[i].setVisible(false);
//                    if (useWhiterPin) {
//                        bottomLetterLbl[i].setColor(Color.BLACK);
//                    }
//                }
//
//
//                float silentTime = 0;
//                boolean intervalTimeFlag = true;
//                if (isLianxian) {
//                    isLianxian = false;
//
//                    if (stringBuilder.length() >= 3) {
////						PlatformManager.instance.showLog("DDNAOperationIntervalTime:"+gameData.DDNAOperationIntervalTime);
//                        //3个词的不用加时间
//                        if (gameData.canOutputMayFeelHard) {
//                            if (ddnaOperationIntervalTimeCompareValue >= 0 && gameData.DDNAOperationIntervalTime > ddnaOperationIntervalTimeCompareValue) {
//                                needToShowDNAPropRewardPanel = true;
//                                PlatformManager.instance.mayFeelHard(levelType, gameData.DDNALevelText, "silent", (int) gameData.DDNAOperationIntervalTime, 0);
//                                //输出数据
//                                intervalTimeFlag = false;
//                            }
//                        }
//
//                        silentTime = gameData.DDNAOperationIntervalTime;
//                        gameData.DDNAOperationIntervalSaveTime.append(gameData.DDNAOperationIntervalTime + ",");
//                        gameData.DDNAOperationIntervalTime = 0;
//                    } else {
//                        //小于等于3个词的需要加时间
//                        gameData.DDNAOperationIntervalTime += gameData.DDNALianciTime;
//                        silentTime = gameData.DDNAOperationIntervalTime;
//                    }
//
//                    gameData.DDNAerrorBeforeFindWord += gameData.DDNALianciTime;
//                    gameData.DDNALianciTime = 0;
//
//
//                    if (!isCheckWord && !intervalTimeFlag) {
//                        //2个都没找出来就输出false，checkWord在后面，所以不用管
//                        gameData.canOutputMayFeelHard = false;
//                    }
//
//                    checkWord((int) silentTime);
//                }
//
//            }
//
//
//        };
//        stage.addListener(inputListener);
//
//        initThemeRegions();
//
//        initDi();
//        initCombo();
//
//        //先初始化下面的字母再初始化上面的布局
//        initBottomLetters();
////
//
//
////
//        initBuju();
//        initPreview();
//        initFlyLbl();
////
//
//        CommonGroupLoad();
//
//        initMyLine();
//
//        initYindaoXuxian();
//
//
//    }
//
//    private boolean videoBoxShow = false;
//
//    Position videoBoxPos = new Position();
//    private float videoBoxScale = 1;
//    private float gemScale = 1;
//
//    private boolean showVideoBox(Position temp){
//        if(videoBoxShow) return false;
//        if(!PlatformManager.instance.isRewaedVideoReady()) return false;
//        if(videoBoxPos.x !=-1 || videoBoxPos.y != -1) return false;
//        if(!GameData.instance.videoBoxEnable){
//
//            int limit = 2;
//            if(RuntimeData.instance.exhelpNum == 3 || RuntimeData.instance.exhelpNum==4 || RuntimeData.instance.exhelpNum ==5){
//                limit = 3;
//            }
//            if(GameData.instance.hintCount1>=limit){
//                GameData.instance.videoBoxEnable = true;
//                Prefs.putBoolean("videoBoxEnable",true);
//            }else{
//                return false;
//            }
//        }
//        Position pos = temp;
//        if(pos!=null){
//            videoBox.clearActions();
//            videoBoxShow = true;
//            float posX = geziGroup[pos.x][pos.y].getX(Align.center);
//            float posY = geziGroup[pos.x][pos.y].getY(Align.center);
//            Vector2 tempVec = new Vector2(posX,posY);
//            geziGroup[pos.x][pos.y].getParent().localToStageCoordinates(tempVec);
//            videoBox.setPosition(tempVec.x,tempVec.y,Align.center);
//            videoBox.setAnimation("animation1");
//            geziGroup[pos.x][pos.y].setVisible(false);
//            videoBoxScale = geziSizeX/130F;
//            videoBox.setScale(videoBoxScale);
//            stage.addActor(videoBox);
//            if(questWarngingGroup.isShowing){
//                videoBox.setZIndex(questWarngingGroup.getZIndex());
//            }else if(warningGroup.isShowing){
//                videoBox.setZIndex(warningGroup.getZIndex());
//            }
//            videoBoxPos.set(pos.x,pos.y);
//            videoBoxUsed = false;
//            return true;
//        }else{
//            videoBoxPos.set(-1,-1);
//            return false;
//        }
//    }
//
//
//    //对bujulist里的actor zindex进行排序
//    void sortBujuList() {
//        bujuList.sort(new Comparator<Actor>() {
//            @Override
//            public int compare(Actor s, Actor t1) {
//                if (s.getZIndex() < t1.getZIndex()) {
//                    return -1;
//                } else if (s.getZIndex() == t1.getZIndex()) {
//                    return 0;
//                } else {
//                    return 1;
//                }
//            }
//        });
//    }
//
//
//    //添加bujuGroup的演员
//    void addBujuGroupActor() {
//        sortBujuList();
//
//        for (int i = 0; i < bujuList.size; i++) {
//            if (!bujuList.get(i).getParent().getName().equals("bujuGroup")) {
//                bujuGroup.addActor(bujuList.get(i));
//                bujuList.get(i).setPosition(bujuList.get(i).getX() - bujuGroup.getX(), bujuList.get(i).getY() - bujuGroup.getY());
//            }
//
//        }
//
//    }
//
//    void removeBujuGroupActor() {
//        sortBujuList();
//        for (int i = 0; i < bujuList.size; i++) {
//            Actor actor = bujuList.get(i);
//            if (actor.getParent().getName().equals("bujuGroup")) {
//
//                stage.addActor(actor);
//
//                actor.setPosition(actor.getX() + bujuGroup.getX(), actor.getY() + bujuGroup.getY());
//            }
//        }
//        beeFront();
//    }
//
//    //整个界面各自是否隐藏，true为不隐藏
//    boolean geziState;
//
//    //让格子出来的动作
//    public void setAppearAction() {
//        if(onShow) return;
//        onShow = true;
//        stage.addListener(inputListener);
//        geziState = true;
//
//        addBujuGroupActor();
//        PlatformManager.instance.game.zcSpineGroup.toFront();
//
//        bujuGroup.clearActions();
//
//        setInputProcessor(false);
//        bujuAppearActionB();
//
//
//        pinGroup.clearActions();
//        pinGroup.setScale(0.1F);
//        pinGroup.getColor().a = 0.3F;
//        pinGroup.addAction(Actions.sequence(Actions.visible(true),Actions.parallel(Actions.alpha(1, 0.3F), Actions.scaleTo(1, 1, 0.5F, Interpolation.swingOut))));
//
//
//        if (fasthintBtnPos != null && fasthintBtn.getCanVisible()) {
//            fasthintBtn.clearActions();
//            fasthintBtn.setPosition(ViewportUtils.getLeft() - fasthintBtn.getWidth(), fasthintBtnPos.y);
//            fasthintBtn.addAction(Actions.sequence(Actions.visible(true)
//                    , Actions.delay(Constants.DoubleAnimationGap), Actions.moveTo(fasthintBtnPos.x, fasthintBtnPos.y, 0.2f, Interpolation.pow2Out)));
//
//        }
//
//        if (fingerBtnPos != null && fingerBtn.getCanVisible()) {
//            fingerBtn.clearActions();
//            fingerBtn.setPosition(ViewportUtils.getLeft() - fingerBtn.getWidth(), fingerBtnPos.y);
//            fingerBtn.addAction(Actions.sequence( Actions.visible(true)
//                    , Actions.moveTo(fingerBtnPos.x, fingerBtnPos.y, 0.3f, Interpolation.pow2Out)));
//        }
//
//        if(spellingBeeBtn!=null){
//            spellingBeeBtn.clearActions();
//            spellingBeeBtn.setScale(0,0);
//            spellingBeeBtn.addAction(Actions.sequence( Actions.visible(true)
//                    , Actions.scaleTo(1,1, 0.3f, Interpolation.swingOut)));
//        }
//
//
//        //右边的按钮
//
//        if (dicBtnCanVisible) {
//            if (gameData.findWordLength > 0 || gameData.findExtraWordLength > 0) {
//                dictBtn.clearActions();
//                dictBtn.setColor(1,1,1,0);
//                dictBtn.addAction(Actions.sequence(Actions.visible(true),Actions.alpha(1,0.3f, Interpolation.pow2Out)));
//            }
//        }
//
//        if (dailyProgressPos != null) {
//            dailyProgress.clearActions();
//            dailyProgress.setColor(1,1,1,0);
//            dailyProgress.addAction(Actions.sequence(Actions.visible(true),Actions.alpha(1, 0.3f, Interpolation.pow2Out)));
//        }
//
//        if(videoBtn.getCanVisible()){
//            videoBtn.clearActions();
//            if(PlatformManager.instance.isRewaedVideoReady()) {
//                setVideoBtnClosed(false);
//            }else {
//                setVideoBtnClosed(true);
//            }
//        }
//
//
//        if (hintBtnPos != null && hintBtn.getCanVisible()) {
//            hintBtn.clearActions();
//            hintBtn.setPosition(ViewportUtils.getRight(), hintBtnPos.y);
//            hintBtn.addAction(Actions.sequence(Actions.visible(true)
//                    , Actions.delay(Constants.DoubleAnimationGap), Actions.moveTo(hintBtnPos.x, hintBtnPos.y, 0.3f, Interpolation.pow2Out)
//            ));
//        }
//
//        if (shuffleBtnPos != null && shuffleBtn.getCanVisible()) {
//            shuffleBtn.clearActions();
//            shuffleBtn.setPosition(ViewportUtils.getRight(), shuffleBtnPos.y);
//            shuffleBtn.addAction(Actions.sequence( Actions.visible(true)
//                    , Actions.moveTo(shuffleBtnPos.x, shuffleBtnPos.y, 0.3f, Interpolation.pow2Out)
//            ));
//        }
//
//
//        if (levelBtnPos != null) {
//            levelBtn.clearActions();
//            levelBtn.setPosition(ViewportUtils.getLeft() - levelBtn.getBgWidth(), levelBtnPos.y);
//            levelBtn.setColor(1,1,1,0);
//            levelBtn.addAction(Actions.sequence(Actions.visible(true),
//                    Actions.parallel(
//                            Actions.moveTo(levelBtnPos.x, levelBtnPos.y, 0.2f, Interpolation.pow2Out)
//                            ,Actions.alpha(1,0.3F)
//                            ,Actions.run(()->{
//                                if (levelBtn.getText().equals("")) {
//                                    levelBtn.setText(levelText);
//                                }
//                            })
//                    )));
//        }
//
//
//
//        if (coinGroupPos != null && coinGroup.getCanVisible()) {
//            PlatformManager.instance.getCoinGroup().show(0.3F);
//        }
//    }
//
//    private void bujuAppearActionB(){
//        float deltaTime = 0.06F;
//        float geziAppearTime = 0.3F;
//        float max = 0;
//        float [][] delayTime = new float[geziGroup.length][geziGroup[0].length];
//        for(int i = 0;i<wordCount;i++){
//            int dir = MyEnum.dirToInt(wordMsg[i].dir);
//            for (int j = 0; j < wordMsg[i].length; j++) {
//                float oldTime = delayTime[wordMsg[i].x + dir * j][wordMsg[i].y + (1 - dir) * j];
//                float newTime = j * deltaTime + i * 0.0F;
//                if(oldTime == 0 || newTime>oldTime){
//                    delayTime[wordMsg[i].x + dir * j][wordMsg[i].y + (1 - dir) * j] = newTime;
//                }
//                max = Math.max(max,newTime);
//            }
//
//        }
//        resetBujuState();
//        bujuGroup.setScale(1);
//        videoBox.clearActions();
//        bujuGroup.addAction(Actions.sequence(Actions.visible(true),Actions.run(()->{
//            for(int i = 0;i<wordCount;i++){
//                int dir = MyEnum.dirToInt(wordMsg[i].dir);
//                for (int j = 0; j < wordMsg[i].length; j++) {
//                    int x = wordMsg[i].x + dir * j;
//                    int y = wordMsg[i].y + (1 - dir) * j;
//                    geziGroup[x][y].addAction(Actions.delay(delayTime[x][y], Actions.scaleTo(1, 1, geziAppearTime, Interpolation.swingOut)));
//                    if(videoBoxPos.x == x && videoBoxPos.y == y){
//                        videoBox.addAction(Actions.delay(delayTime[x][y], Actions.scaleTo(videoBoxScale, videoBoxScale, geziAppearTime, Interpolation.swingOut)));
//                    }
//                    letter[x][y].addAction(Actions.delay(delayTime[x][y], Actions.scaleTo(1, 1, geziAppearTime, Interpolation.swingOut)));
//                    if(coinGeziImages[x][y]!=null) {
//                        coinGeziImages[x][y].addAction(Actions.delay(delayTime[x][y], Actions.scaleTo(1, 1, geziAppearTime, Interpolation.swingOut)));
//                    }
//
//                    int beeIndex = getBeeIndex(x,y);
//                    if(beeIndex!=-1){
//                        bees[beeIndex].addAction(Actions.delay(delayTime[x][y], Actions.scaleTo(beeScale, beeScale, geziAppearTime, Interpolation.swingOut)));
//                    }
//                }
//            }
//            if(feng!=null &&fengPosition.x!=-1 && fengPosition.y!=-1) {
//                feng.addAction(Actions.delay(delayTime[fengPosition.x][fengPosition.y], Actions.scaleTo(gemScale, gemScale, geziAppearTime, Interpolation.swingOut)));
//            }
//        })));
//
//        bujuGroup.addAction(Actions.delay(max + geziAppearTime,Actions.run(()->{
//            removeBujuGroupActor();
//            pinGroup.toFront();
//            setInputProcessor(true);
//            setYindao();
//
//            if (questWarngingGroup.isShowing) {
//                questWarngingGroup.toFront();
//            }
//
//            PlatformManager.instance.game.zcSpineGroup.toFront();
//            if(!yindaoPanel.isShowing && !checkStartLevelPopup && !(gameData.gameIs == Constants.fasthintStart && !gameData.isYindaoed[15])){
//                checkStartLevelPopup = true;
//                if(SpellingBeeManager.getInstance().getState() == SpellingBeeManager.State.RESULT){
//                    spellingBeePanel.setTrigger("START_LEVEL");
//                    showPanel(spellingBeePanel);
//                } else{
//                    boolean flag = checkPopupPanel("START_LEVEL2");
//                    if (!flag) {
//                        flag = checkPopupPanel("START_LEVEL");
//                        if (!flag) {
//                            int dailyGuideDate = Prefs.getInteger("daily_guide_date", 0);
//                            if (gameMode == MyEnum.GameMode.normalMode && gameIs > 12 && RuntimeData.instance.levelStartCount == 3 && StateManager.judgeDailyHasNotFinish() && dailyGuideDate != CalendarUtils.getToday()) {
//                                showPanel(dailyGuidePanel);
//                                Prefs.putInteger("daily_guide_date", CalendarUtils.getToday());
//                                Prefs.flush();
//                            }
//                        }
//                    }
//                }
//            }
//        })));
//    }
//
//
//    private void resetBujuState(){
//        for(int i = 0;i<wordCount;i++) {
//            int dir = MyEnum.dirToInt(wordMsg[i].dir);
//            for (int j = 0; j < wordMsg[i].length; j++) {
//                int x = wordMsg[i].x + dir * j;
//                int y = wordMsg[i].y + (1 - dir) * j;
//                geziGroup[x][y].clearActions();
//                letter[x][y].clearActions();
//
//                geziGroup[x][y].setScale(0);
//                letter[x][y].setScale(0);
//
//                letter[x][y].setRotation(0);
//                geziGroup[x][y].setRotation(0);
//
//                geziGroup[x][y].getColor().a = 1;
//                letter[x][y].getColor().a = 1;
//
//                if(videoBoxPos.x == x && videoBoxPos.y == y){
//                    videoBox.getColor().a = 1;
//                    videoBox.setRotation(0);
//                    videoBox.setScale(0);
//                }
//                if(coinGeziImages[x][y]!=null) {
//                    coinGeziImages[x][y].clearActions();
//                    coinGeziImages[x][y].getColor().a = 1;
//                    coinGeziImages[x][y].setRotation(0);
//                    coinGeziImages[x][y].setScale(0);
//                }
//
//            }
//        }
//
//        if(beePositions!=null){
//            for(int i = 0;i<beePositions.length;i++){
//                bees[i].clearActions();
//                bees[i].getColor().a = 1;
//                bees[i].setRotation(0);
//                bees[i].setScale(0);
//                if(isBeeEnable(i)) {
//                    bees[i].setVisible(true);
//                }else{
//                    bees[i].setVisible(false);
//                }
//            }
//        }
//
//        if(feng!=null &&fengPosition.x!=-1 && fengPosition.y!=-1) {
//            feng.clearActions();
//            feng.setVisible(true);
//            feng.getColor().a = 1;
//            feng.setRotation(0);
//            feng.setScale(0);
//            feng.setOrigin(Align.center);
//        }
//    }
//
//
//    private void bujuHideActionB(){
//        float rotateTime = 0.3F;
//        float hideTime = 0.3F;
//        for(int i = 0;i<wordCount;i++){
//            int dir = MyEnum.dirToInt(wordMsg[i].dir);
//            for (int j = 0; j < wordMsg[i].length; j++) {
//                int x = wordMsg[i].x + dir * j;
//                int y = wordMsg[i].y + (1 - dir) * j;
//                if(x == videoBoxPos.x && y == videoBoxPos.y){
//                    videoBox.clearActions();
//                    videoBox.addAction(Actions.parallel(Actions.rotateBy(45,rotateTime),Actions.scaleTo(0,0,hideTime),Actions.alpha(0,hideTime)));
//                }
//                geziGroup[x][y].addAction(Actions.parallel(Actions.rotateBy(45,rotateTime),Actions.scaleTo(0,0,hideTime),Actions.alpha(0,hideTime)));
//                letter[x][y].addAction(Actions.parallel(Actions.rotateBy(45,rotateTime),Actions.scaleTo(0,0,hideTime),Actions.alpha(0,hideTime)));
//                if(coinGeziImages[x][y]!=null) {
//                    coinGeziImages[x][y].setOrigin(Align.center);
//                    coinGeziImages[x][y].addAction(Actions.parallel(Actions.rotateBy(45, rotateTime), Actions.scaleTo(0, 0, hideTime), Actions.alpha(0, hideTime)));
//                }
//
//                int beeIndex = getBeeIndex(x,y);
//                if(beeIndex!=-1){
//                    bees[beeIndex].addAction(Actions.parallel(Actions.rotateBy(45, rotateTime), Actions.scaleTo(0, 0, hideTime), Actions.alpha(0, hideTime)));
//                }
//            }
//        }
//        if(feng!=null){
//            feng.addAction(Actions.parallel(Actions.rotateBy(45, rotateTime), Actions.scaleTo(0, 0, hideTime), Actions.alpha(0, hideTime)));
//        }
//    }
//
//    //返回隐藏动作时间
//    float setHideAction(boolean isCoinGroupAction) {
//        if(!onShow) return 0;
//        onShow = false;
//        stage.removeListener(inputListener);
//        geziState = false;
//
//        guliciSpineGroup.setVisible(false);
//        comboGroup.setVisible(false);
//        addBujuGroupActor();
//
//        setInputProcessor(false);
//
//        runDelay(()->{
//            if (!isJiesuan) {
//                setInputProcessor(true);
//            }
//        }, 0.41f + 2 * Constants.DoubleAnimationGap);
//
//        bujuHideActionB();
//        pinGroup.clearActions();
//        pinGroup.addAction(Actions.parallel(Actions.alpha(0.2F, 0.3F), Actions.sequence(Actions.scaleTo(0.2F, 0.2F, 0.2F), Actions.visible(false))));
//
//        float deltaX = 18;
//
//        //左边的按钮
//
//        if (fasthintBtnPos != null && fasthintBtn.getCanVisible()) {
//            fasthintBtn.clearActions();
//            fasthintBtn.addAction(Actions.sequence(Actions.moveTo(fasthintBtnPos.x, fasthintBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getLeft() - fasthintBtn.getWidth(), fasthintBtnPos.y, 0.3f), Actions.visible(false)));
//        }
//
//
//        if (fingerBtnPos != null && fingerBtn.getCanVisible()) {
//            fingerBtn.clearActions();
//            fingerBtn.addAction(Actions.sequence(Actions.moveTo(fingerBtnPos.x, fingerBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getLeft() - fingerBtn.getWidth(), fingerBtnPos.y, 0.3f), Actions.visible(false)));
//        }
//
//
//
//        if(videoBtn.getCanVisible()){
//            setVideoBtnClosed(true);
//        }
//
//
//        if (hintBtnPos != null && hintBtn.getCanVisible()) {
//            hintBtn.clearActions();
//            hintBtn.addAction(Actions.sequence(Actions.moveTo(hintBtnPos.x, hintBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getRight(), hintBtnPos.y, 0.3f), Actions.visible(false)));
//        }
//
//        if (shuffleBtnPos != null && shuffleBtn.getCanVisible()) {
//            shuffleBtn.clearActions();
//            shuffleBtn.addAction(Actions.sequence(Actions.moveTo(shuffleBtnPos.x, shuffleBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getRight(), shuffleBtnPos.y, 0.3f), Actions.visible(false)));
//        }
//
//        float deltaY = 18;
//        //上边的按钮
//
//
//        if (levelBtnPos != null) {
//            levelBtn.clearActions();
//            levelBtn.setPosition(levelBtnPos.x, levelBtnPos.y);
//            levelBtn.addAction(Actions.parallel(
//                    Actions.moveTo(ViewportUtils.getLeft() - levelBtn.getWidth(), levelBtnPos.y, 0.3f),
//                    Actions.alpha(0,0.3F)
//            ));
//        }
//
//        if (dailyProgressPos != null) {
//            dailyProgress.clearActions();
//            dailyProgress.addAction(Actions.alpha(0, 0.3f));
//        }
//
//        if (dicBtnCanVisible) {
//            if (gameData.findWordLength > 0 || gameData.findExtraWordLength > 0) {
//                dictBtn.addAction(Actions.sequence(Actions.alpha(0,0.3F),Actions.visible(false)));
//            }
//        }
//
//
//        if(spellingBeeBtn!=null){
//            spellingBeeBtn.clearActions();
//            spellingBeeBtn.addAction(Actions.sequence(Actions.scaleTo(1.05F, 1.05F,0.1F)
//                    , Actions.scaleTo(0, 0, 0.2f), Actions.visible(false)));
//        }
//
//        return 0.3f;
//    }
//
//    void gameEndBtnHide(){
//        if (fasthintBtnPos != null && fasthintBtn.getCanVisible()) {
//            fasthintBtn.clearActions();
//            fasthintBtn.addAction(Actions.sequence(Actions.moveTo(fasthintBtnPos.x, fasthintBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getLeft() - fasthintBtn.getWidth(), fasthintBtnPos.y, 0.2f), Actions.visible(false)));
//        }
//
//
//        if (fingerBtnPos != null && fingerBtn.getCanVisible()) {
//            fingerBtn.clearActions();
//            fingerBtn.addAction(Actions.sequence(Actions.moveTo(fingerBtnPos.x, fingerBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getLeft() - fingerBtn.getWidth(), fingerBtnPos.y, 0.2f), Actions.visible(false)));
//        }
//
//        if(videoBtn.getCanVisible()){
//            setVideoBtnClosed(true);
//        }
//
//
//        if (hintBtnPos != null && hintBtn.getCanVisible()) {
//            hintBtn.clearActions();
//            hintBtn.addAction(Actions.sequence(Actions.moveTo(hintBtnPos.x, hintBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getRight(), hintBtnPos.y, 0.2f), Actions.visible(false)));
//        }
//
//        if (shuffleBtnPos != null && shuffleBtn.getCanVisible()) {
//            shuffleBtn.clearActions();
//            shuffleBtn.addAction(Actions.sequence(Actions.moveTo(shuffleBtnPos.x, shuffleBtnPos.y)
//                    , Actions.moveTo(ViewportUtils.getRight(), shuffleBtnPos.y, 0.2f), Actions.visible(false)));
//        }
//        if(spellingBeeBtn!=null){
//            spellingBeeBtn.clearActions();
//            spellingBeeBtn.addAction(Actions.sequence(Actions.scaleTo(1.05F, 1.05F,0.1F)
//                    , Actions.scaleTo(0, 0, 0.2f), Actions.visible(false)));
//        }
//
//    }
//
//
//    //判断是不是有单词在飞
//    int isFly = 0;
//    //设置飞行速度
//    final float flySpeed = 2000;
//    //相邻2个格子间到达的时间间隔
//    final float gapFlyTime = 0.03f;
//    Interpolation flyIntepolation = Interpolation.fade;
//    //	final float flySpeed = 2000;
//    float maxFlyTime;
//
//
//    private boolean shouldCheckJiesuan = true;
//    //让单词飞上去的动作
//    //返回动作的时间
//    float setWordFly(int index) {
//        shouldCheckJiesuan = true;
//        float width = 0;
//        maxFlyTime = 0;
//
//        int cnt = 0;
//        for (int i = 0; i < flyMaxn && cnt < gameAnswer[index].length(); i++) {
//            if (!useFly[i]) {
//                useFly[i] = true;
//                flyShunxu[cnt++] = i;
//            }
//        }
//
//        for (int j = 0; j < gameAnswer[index].length(); j++) {
//            flyLbl[flyShunxu[j]].setText(gameAnswer[index].charAt(j) + "");
//            width += flyLbl[flyShunxu[j]].getPrefWidth();
//        }
//        int pos[] = wordToGrid(index);
//
//        if (isYindaoWithoutState) {
//            for (int j = 0; j < pos.length / 2; j++) {
//                if (yindaoGeziImage[pos[2 * j]][pos[2 * j + 1]] != null) {
//                    yindaoGeziImage[pos[2 * j]][pos[2 * j + 1]].setVisible(false);
//                }
//            }
//        }
//
//
//        flyLblGroup[flyShunxu[0]].setPosition(360 - width / 2, preview.getLabelY());
//        double dis = Math.sqrt((double) (MyMathUtils.getPow(letter[pos[0]][pos[1]].getX() + halfGeziSizeX + offsetX[gameAnswer[index].charAt(0) - 'A'] * bujuFontScale - flyLblGroup[flyShunxu[0]].getX(), 2)
//                + MyMathUtils.getPow(letter[pos[0]][pos[1]].getY() + halfGeziSizeY + offsetY[gameAnswer[index].charAt(0) - 'A'] * bujuFontScale - flyLblGroup[flyShunxu[0]].getY(), 2)));
//
//        maxFlyTime = Math.max((float) dis / flySpeed, maxFlyTime);
//
//
//        for (int j = 1; j < gameAnswer[index].length(); j++) {
//            flyLblGroup[flyShunxu[j]].setPosition(flyLblGroup[flyShunxu[j - 1]].getX() + flyLbl[flyShunxu[j - 1]].getPrefWidth(), preview.getLabelY());
//
//            dis = Math.sqrt((double) (MyMathUtils.getPow(letter[pos[2 * j]][pos[2 * j + 1]].getX() + halfGeziSizeX + offsetX[gameAnswer[index].charAt(j) - 'A'] * bujuFontScale - flyLblGroup[flyShunxu[j]].getX(), 2)
//                    + MyMathUtils.getPow(letter[pos[2 * j]][pos[2 * j + 1]].getY() + halfGeziSizeY + offsetY[gameAnswer[index].charAt(j) - 'A'] * bujuFontScale - flyLblGroup[flyShunxu[j]].getY(), 2)));
//            maxFlyTime = Math.max((float) dis / flySpeed, maxFlyTime);
//        }
//
//
//        maxFlyTime = Math.min(0.4f, maxFlyTime);
//        maxFlyTime = Math.max(0.25f, maxFlyTime);
//
//
////		System.out.println(maxFlyTime);
//
//        for (int j = 0; j < pos.length / 2; j++) {
//            flyLbl[flyShunxu[j]].setText(gameAnswer[index].charAt(j) + "");
////			flyLbl[j].setColor(Color.BLACK);
//
//
//            float scaleV = geziSizeX / 50;
////			float scaleV = (geziSizeX +25) / 40f;
//            flyLblGroup[flyShunxu[j]].setScale(geziSizeX / 50);
//
//
////			float scaleV = (geziSizeX*1.2f) / 40f;
////			flyLblGroup[flyShunxu[j]].setScale((geziSizeX*1.2f) /40f);
//            flyLblGroup[flyShunxu[j]].setSize(flyLbl[flyShunxu[j]].getPrefWidth(), flyLbl[flyShunxu[j]].getPrefHeight());
//            flyLblGroup[flyShunxu[j]].setOrigin(Align.center);
//
//
//            flyLblGroup[flyShunxu[j]].toFront();
//
//            final int t = j;
//
//
//            isFly += flyShunxu[j];
//            endVideoBox(pos[2 * t], pos[2 * t + 1]);
//
//            float flyTime = maxFlyTime + gapFlyTime * j;
//            float offset = -2;
//            flyLblGroup[flyShunxu[j]].addAction(Actions.sequence(Actions.scaleTo(1f, 1f), Actions.visible(true)
//                    , Actions.parallel(Actions.moveToAligned(offset + letter[pos[2 * j]][pos[2 * j + 1]].getX() + halfGeziSizeX + offsetX[gameAnswer[index].charAt(j) - 'A'] * bujuFontScale, offset+ letter[pos[2 * j]][pos[2 * j + 1]].getY() + halfGeziSizeY + offsetY[gameAnswer[index].charAt(j) - 'A'] * bujuFontScale, Align.center, flyTime, flyIntepolation)
//                            , Actions.scaleTo(scaleV, scaleV, flyTime, Interpolation.pow2Out))
//                    , Actions.run(()->{
//                        if (isSkinAlwaysBlack) {
//                            letterLbl[pos[2 * t]][pos[2 * t + 1]].setColor(Color.BLACK);
//                        } else if (useKuaiB) {
//                            letterLbl[pos[2 * t]][pos[2 * t + 1]].setColor(Color.WHITE);
//                        }
//
//                        Vector2 temp = lightGezi(pos[2 * t], pos[2 * t + 1], 0f, false, flyShunxu[t]);
//                        if(temp.x!=-1 && temp.y!=-1){
//                            shouldCheckJiesuan = false;
//                        }
//
//                        if (t == pos.length / 2 - 1) {
//                            getEmptyGrid(-1);
//                            showCombo();
//                        }
//                    }),
//                    Actions.visible(false)
//                    , Actions.run(()->{
//                        useFly[flyShunxu[t]] = false;
//                        isFly -= flyShunxu[t];
//                    })));
//
//        }
//
//        if(wordState != passWordState) {
//            checkPopupPanel("FIND_WORD");
//        }
//        return maxFlyTime;
//    }
//
//    private boolean videoBoxUsed;
//
//    private void endVideoBox(int x,int y){
//        if(x!=videoBoxPos.x || y != videoBoxPos.y) return;
//        if(videoBoxPos.x == -1 || videoBoxPos.y == -1) return;
//        geziGroup[videoBoxPos.x][videoBoxPos.y].setVisible(true);
//        videoBoxPos.set(-1,-1);
//        videoBox.setAnimation("animation3");
//        if(!videoBoxUsed){
//            GameData.instance.hintCount1 = 0;
//            Prefs.putInteger("hintCount1",0);
//            GameData.instance.videoBoxEnable = false;
//            Prefs.putBoolean("videoBoxEnable",false);
//        }
//    }
//
//
////    //将找出的整个单词连起来,如果能十字相连就返回这个单词能十字相连几个单词
////    int setWordAppear(int index) {
////        if (isFind(index)) {
////            return 0;
////        }
////        int canCrossWord = 0;
////        int tx = wordMsg[index].x, ty = wordMsg[index].y;
////        int kx = 0, ky = 0;
////        if (wordMsg[index].dir == MyEnum.Dir.horizontal) {
////            kx = 0;
////            ky = 1;
////        } else {
////            kx = 1;
////            ky = 0;
////        }
////        for (int j = 0; j < wordMsg[index].length; j++) {
////            int xx = tx + kx * j;
////            int yy = ty + ky * j;
////            //当这个方格之前没字的时候，检测另一方向的是不是也弄好了
////            if (gridMsg[xx][yy].appear == MyEnum.WordAppear.notAppear) {
////                gridMsg[xx][yy].appear = MyEnum.WordAppear.appear;
////                if (wordMsg[index].dir == MyEnum.Dir.horizontal) {
////                    canCrossWord += checkCrossWord(xx, yy, MyEnum.Dir.vertical);
////                } else {
////                    canCrossWord += checkCrossWord(xx, yy, MyEnum.Dir.horizontal);
////                }
////            }
////            gridMsg[xx][yy].appear = MyEnum.WordAppear.appear;
////            addActionOnLetter(wordMsg[index].dir, xx, yy, j * lightGeziDelay, geziGroup[xx][yy], true, false, -1);
////        }
////        return canCrossWord;
////    }
//
//
//    private Vector2 tmpVector2 = new Vector2();
//
//
//    private void setWordState(int i){
//        setWordState(i,false);
//    }
//
//
//
//    //设置单词已经找到
//    private void setWordState(int i,boolean link) {
//        if (isFind(i)) {
//            return;
//        }
//        allWordPanel.addWord(wordMsg[i].str, false);
//
//
//        if (isNewLevel) {
//            gameData.questWordSpllingNum++;
//            Prefs.putInteger("questWordSpllingNum", gameData.questWordSpllingNum);
//
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 9) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailyWordSpllingNum++;
//                    Prefs.putInteger("dailyWordSpllingNum", gameData.dailyWordSpllingNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("questWordSpllingNum", 0);
//            showQuestWarningGroup("dailyWordSpllingNum", 1);
//        }
//
//        if (gameData.gameIs == 29 && !gameData.hasShowDicPanel && gameMode == MyEnum.GameMode.normalMode) {
//            Prefs.putBoolean("hasShowDicPanel", true);
//            Prefs.flush();
//            gameData.hasShowDicPanel = true;
//
//
//            int pos[] = wordToGrid(i);
//            float tx, ty;
//            if (wordMsg[i].dir == MyEnum.Dir.vertical) {
//
//                tx = geziGroup[pos[0]][pos[1]].getX();
//                ty = geziGroup[pos[pos.length - 2]][pos[pos.length - 1]].getY();
//            } else {
//                //水平
//                tx = (geziGroup[pos[0]][pos[1]].getX() + geziGroup[pos[pos.length - 2]][pos[pos.length - 1]].getX()) / 2;
//                ty = geziGroup[pos[0]][pos[1]].getY();
//            }
//
//
//            dictBtn.toFront();
//            for (int k0 = 0; k0 < pos.length / 2; k0++) {
//                geziGroup[pos[2 * k0]][pos[2 * k0 + 1]].toFront();
//                letter[pos[2 * k0]][pos[2 * k0 + 1]].toFront();
//            }
//
////							gameData.setYindao(7);
//        }
//
//
//        if (!dictBtn.isVisible() && dicBtnCanVisible) {
//            dictBtn.setColor(1,1,1,0);
//            dictBtn.setVisible(true);
//            dictBtn.addAction(Actions.alpha(1,0.3F));
//
//        }
//
//        //添加已经找到的单词
//        gameData.findWord[gameData.findWordLength] = gameAnswer[i];
//        gameData.findWordLength++;
//
//        addComboTimes++;
//        wordState |= (1 << i);
//        notPassSet.remove(i);
//        if (gameMode != MyEnum.GameMode.normalMode) {
//            if (fengPosition.index == i || fengPosition.index1 == i) {
//                addDailyJindu();
//                //单词到格子有间隔
//                float fengDelayTime = wordMsg[i].length * gapFlyTime + 0.1f;
//                Gemstone stone = new Gemstone(feng.getId());
//                stone.setPosition(feng.getX(),feng.getY());
//                stage.addActor(stone);
//                stone.setScale(gemScale);
//                feng.setVisible(false);
//                float moveTime = 0.7F;
//                int gemNum = Prefs.getInteger("gemNum",0);
//                gemNum++;
//                Prefs.putInteger("gemNum",gemNum);
//                stone.addAction(Actions.sequence(
//                        Actions.delay(fengDelayTime)
//                        ,Actions.parallel(Actions.scaleTo(gemScale * 1.2F,gemScale*1.2F,0.2F),Actions.moveBy(0,-30,0.2F,Interpolation.fastSlow),Actions.run(()->{AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_CATCH_PATH, Sound.class)); }))
//                        ,Actions.parallel(
//                                Actions.rotateBy(360,moveTime),
//                                Actions.delay(0.5F,Actions.scaleTo(0.2F,0.2F,moveTime-0.5F)),
//                                Actions.moveToAligned(dailyProgress.getFengX(), dailyProgress.getFengY(), Align.center,moveTime, Interpolation.slowFast)
//                                //                                        Actions.delay(0.5F,Actions.alpha(0,0.5F))
//                        ), Actions.run(()->{
//                            dailyProgress.setProgress(dailyJindu);
//                            dailyProgress.light();
//                            AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_HIT_PATH, Sound.class));
//                        }),Actions.removeActor()));
//
//            }
//        }else{
//            if(beePositions!=null) {
//                if (wordMsg[i].str.equals(maxLengthWord)) {
//                    for (int m = 0; m < beePositions.length; m++) {
//                        beeFly(m, 0.1F * m);
//                    }
//                } else {
//                    for (int m = 0; m < beePositions.length; m++) {
//                        if (beePositions[m].index == i || beePositions[m].index1 == i) {
//                            beeFly(m, 0);
//                            break;
//                        }
//                    }
//                    if(link) {
//                        beeDisappear();
//                    }
//                }
//            }
//        }
//    }
//
//    private int beeShow= 0;
//
//    Vector2 beeTargetPos;
//    private boolean beeFly(int index,float delay){
//        if(index == -1) return false;
//        if(isBeeEnable(index)){
//            if(delay == 0){
//                beeShow = GameData.instance.spellingBees;
//                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_CATCH_PATH, Sound.class));
//            }
//            GameData.instance.spellingBees++;
//            levelBees++;
//            Prefs.putInteger("levelBees",levelBees);
//            Prefs.putInteger("spellingBees",GameData.instance.spellingBees);
//            bees[index].setAnimation("fly");
//            beeState |= (1 << index);
//            bees[index].toFront();
//            if(beeTargetPos == null){
//                beeTargetPos = new Vector2(spellingBeeBtn.getX(Align.center),spellingBeeBtn.getY(Align.center));
//            }
//            bees[index].addAction(Actions.sequence(Actions.delay(delay),Actions.moveBy(0,30,0.2F),Actions.delay(0.2F),Actions.run(()->{
//                bees[index].toFront();
//            }),Actions.moveTo(beeTargetPos.x,beeTargetPos.y,0.4F),Actions.run(()->{
//                bees[index].addAction(Actions.scaleTo(0,0,0.1F));
//                spellingBeeBtn.addAction(Actions.sequence(Actions.scaleTo(1.1F,1.1F,0.06F),Actions.scaleTo(1,1,0.06F)));
//                beeShow++;
//                if(beeShow>GameData.instance.spellingBees){
//                    beeShow = GameData.instance.spellingBees;
//                }
//                spellBeeLbl.setText(beeShow+"");
//            })));
//            Prefs.putInteger("beeState",beeState);
//            return true;
//        }
//        return false;
//    }
//
//
//    private void beeDisappear(){
//        if(beePositions == null) return;
//        int first = maxLengthWord.length() == 6?2:1;
//        int start = beeDisappearCount==0?0:first + (beeDisappearCount-1)*2;
//
//        //振翅蜜蜂
//        for(int i = beePositions.length-start - 1;i>=beePositions.length-first - beeDisappearCount *2 && i>=0;i--){
//            if(!isBeeEnable(i)){
//                continue;
//            }
//            bees[i].toFront();
//            bees[i].setAnimation("fly");
//        }
//
//        int tempBeeDisappearCount = beeDisappearCount-1;
//        if(tempBeeDisappearCount>=0){
//            start = tempBeeDisappearCount==0?0:first + (tempBeeDisappearCount-1)*2;
//            //渐隐蜜蜂
//            for(int i = beePositions.length-start - 1;i>=beePositions.length-first - tempBeeDisappearCount *2 && i>=0;i--){
//                if(!isBeeEnable(i)){
//                    continue;
//                }
//                beeState |= (1 << i);
//                bees[i].toFront();
//                bees[i].addAction(Actions.parallel(Actions.alpha(0,0.7F),Actions.moveBy(0,30,0.7F)));
//            }
//        }
//
//
//        beeDisappearCount++;
//        Prefs.putInteger("beeState",beeState);
//
//    }
//
//
//    private void addDailyJindu(){
//        dailyJindu++;
//        gameData.dailyJindu = dailyJindu;
//        if (gameMode == MyEnum.GameMode.dailyMode) {
//            Prefs.putInteger("dailyJindu", dailyJindu);
//            Prefs.flush();
//        }
//    }
//
//    //设置皮肤块状态
//    void setSkinState(int i) {
//        skinState |= (1 << i);
//    }
//
//    //判断皮肤块是不是连起来了，返回true表示连起来了
//    boolean isFindSkin(int index) {
//        if ((skinState & (1 << index)) != 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    int rank[];
//    float originX, originY;
//
//
//    //下面单词的位置，用于连线时候在stage中的实际位置反应
//    Group bottomLettersGroup[];
//    Label bottomLetterLbl[];
//    Group lettersGroup;
//
//    boolean[] isLinked;
//    int[] yindaoLetterUseCount;
//
//    void shuffle(int len) {
//
//        //当为第0关且通过关数为0关的时候不调位置
//
//        if (gameData.gameIs == 0 && !gameData.isYindaoed[0]) {
//            return;
//        } else if (gameMode == MyEnum.GameMode.dailyMode && !gameData.isYindaoed[14]) {
//            return;
//        }
//
//        int step = 20;
//        while (step-- >= 0) {
//            int i = random(len - 1);
//            int j = random(len - 1);
//
//            int tmp = rank[i];
//            rank[i] = rank[j];
//            rank[j] = tmp;
//        }
//    }
//
//    //初始化拼装框里的字母
//    Image woodImage[];
//
//    void initBottomLetters() {
//
//
//        //得到木块的宽和高
//        woodWidth = Constants.bottomWoodSize[letterLength];
//        woodHeight = Constants.bottomWoodSize[letterLength];
//
//        originX = 360;
//        originY = 360;
//
//        lettersGroup = new Group();
//
//
//        rank = new int[letterLength];
//        woodImage = new Image[letterLength];
//        bottomLettersGroup = new Group[letterLength];
//        bottomLetterLbl = new Label[letterLength];
//        isLinked = new boolean[letterLength];
//        yindaoLetterUseCount = new int[letterLength];
//        letterIndexBuffer = new int[letterLength];
//
//        for (int i = 0; i < letterLength; i++) {
//            rank[i] = i;
//            isLinked[i] = false;
//        }
//
//
//        BitmapFont bitmapFont = Assets.getInstance().assetManager.get("ui/gameplayNew/font/SFCompactDisplay-Semibold_130_1.fnt", BitmapFont.class);
//        Label.LabelStyle labelStyleB = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());
//        for (int i = 0; i < letterLength; i++) {
//
//            bottomLettersGroup[i] = new Group();
////			bottomLettersGroup[i].setDebug(true);
//
//            woodImage[i] = new Image(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/skin/wood"));
//            woodImage[i].setColor(skinColor);
//            woodImage[i].setSize(woodWidth, woodHeight);
//            pinGroup.addActor(woodImage[i]);
//            woodImage[i].setVisible(false);
//            woodImage[i].setPosition(getPositionXByRank(rank[i], letterLength), getPositionYByRank(rank[i], letterLength));
//
//
//            char zimu = gameLetter.charAt(i);
//
//            bottomLetterLbl[i] = new Label(String.valueOf(zimu), labelStyleB);
//            float fontScale = Constants.bottomLetterFontSizeB[letterLength] / 130f;
//
//            if (zimu == 'W') {
//                //w额外再缩小
//                fontScale *= 0.95f;
//            }
//            bottomLetterLbl[i].setFontScale(fontScale);
////			bottomLetterLbl[i].setColor(65 / 255f, 33 / 255f, 22 / 255f, 1);
//
//            if (useWhiterPin) {
//                bottomLetterLbl[i].setColor(0f, 0f, 0f, 1);
//            } else {
//                bottomLetterLbl[i].setColor(1f, 1f, 1f, 1);
//            }
//
////			bottomLetterLbl[i].setDebug(true);
//
//            bottomLetterLbl[i].setPosition(woodWidth / 2 - bottomLetterLbl[i].getPrefWidth() / 2 + Constants.bottomLetterOffsetX[zimu - 'A'] * fontScale, woodHeight / 2 - bottomLetterLbl[i].getHeight() / 2 + Constants.bottomLetterOffsetY[zimu - 'A'] * fontScale);
////			bottomLetterLbl[i].setPosition(woodWidth/2 - bottomLetterLbl[i].getPrefWidth()/2, woodHeight/2 - bottomLetterLbl[i].getHeight()/2);
//            bottomLettersGroup[i].addActor(bottomLetterLbl[i]);
//
//            bottomLettersGroup[i].setSize(woodImage[i].getWidth(), woodImage[i].getHeight());
//
//
//            bottomLettersGroup[i].setOrigin(Align.center);
//            bottomLettersGroup[i].setPosition(getPositionXByRank(rank[i], letterLength), getPositionYByRank(rank[i], letterLength));
//
//
//            lettersGroup.addActor(bottomLettersGroup[i]);
//            bottomLettersGroup[i].addListener(new ClickListener() {
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_TAP_PATH[0], Sound.class), 0.2f);
//                    return super.touchDown(event, x, y, pointer, button);
//
//                }
//
//            });
//        }
//
//        pinGroup.addActor(lettersGroup);
//
////		for (int i = 0; i < letterLength; i ++){
////			woodImage[i].setZIndex(0);
////		}
//    }
//
//    private void saveWordState(){
//        if(!isNewLevel) return;
//        Prefs.putInteger(stateStr, wordState);
//        if(gameMode == MyEnum.GameMode.normalMode){
//            Prefs.putInteger("beeState",beeState);
//        }
//        Prefs.flush();
//    }
//
//
//    //获得引导手的位置
//    float getYindaoShouXByRank(int i, int len) {
//        return getOriginXByRank(i, len) - 22;
//    }
//
//    float getYindaoShouYByRank(int i, int len) {
//        return getOriginYByRank(i, len) - 145;
//    }
//
//    //获得字母的中间位置
//    private float getOriginXByRank(int i, int len) {
//        return getPositionXByRank(i, len) + woodWidth / 2;
//    }
//
//    private float getOriginYByRank(int i, int len) {
//        return getPositionYByRank(i, len) + woodHeight / 2;
//    }
//
//    private float getPositionXByRank(int i, int len) {
//        // TODO Auto-generated method stub
//
//        float x = Constants.POSITION_X[len - 3][i];
//
//        return x;
//    }
//
//    private float getPositionYByRank(int i, int len) {
//        float y = Constants.POSITION_Y[len - 3][i];
//
//
//        return y;
//    }
//
//
//    /**
//     * 让index序号单词的格子出来
//     *
//     * @param index  单词序号
//     * @param zindex 单词要设置的zindex，如果为最大整数即设置为front，这里的zindex其实是yindaoMask的zindex，当其他演员
//     *               的zindex改变时他也要改变，所以要设置编号
//     */
//    void setWordZindex(int index, int zindex) {
//        int pos[];
//        pos = wordToGrid(index);
////		zindex = zindex < Constants.MAX_INTEGER ? zindex + 1 : zindex;
//        for (int j = 0; j < pos.length / 2; j++) {
//            int x = pos[2 * j];
//            int y = pos[2 * j + 1];
//            geziGroup[x][y].setZIndex(zindex);
//            zindex = zindex < Constants.MAX_INTEGER ? zindex + 1 : zindex;
//
//            if (isYindaoWithoutState) {
//                if (zindex == Constants.MAX_INTEGER) {
//
//
//                    if (skinImages[x][y].isVisible()) {
//                        //皮肤块可见就不把那个显示出来
//                        yindaoGeziImage[x][y].setVisible(false);
//                    } else {
//                        yindaoGeziImage[x][y].setVisible(true);
//                        yindaoGeziImage[x][y].setZIndex(zindex);
//                    }
//                } else {
//                    yindaoGeziImage[x][y].setVisible(false);
//                }
//
//            }
//            letter[x][y].setZIndex(zindex);
//            zindex = zindex < Constants.MAX_INTEGER ? zindex + 1 : zindex;
//        }
//        if (zindex < Constants.MAX_INTEGER) {
//            //因为传入的是zindex - 1，所以最后结果要+1
////			yindaoMask.toFront();
//            yindaoMask.setZIndex(zindex + 1);
//        }
//
//        beeFront();
//
//
//    }
//
//    void addShakeAction(Actor actor) {
//        int shakeRange = 2;
//        float deltaX = -shakeRange;
//        float deltaY = shakeRange;
//        if (actor != null) {
//            actor.addAction(Actions.sequence(Actions.moveBy(deltaX, deltaY, 0.05f)
//                    , Actions.repeat(2, Actions.sequence(Actions.moveBy(-2 * deltaX, -2 * deltaY, 0.05f)
//                            , Actions.moveBy(2 * deltaX, 2 * deltaY, 0.05f)))
//                    , Actions.moveBy(-deltaX, -deltaY, 0.2f)));
//        }
//
//    }
//
//    //重复单词抖动
//    void setRepeatWordShake(int index) {
//        int tx = wordMsg[index].x, ty = wordMsg[index].y;
//        int kx = 0, ky = 0;
//        //等单词为水平方向的时候
//        if (wordMsg[index].dir == MyEnum.Dir.horizontal) {
//            kx = 0;
//            ky = 1;
//        } else if (wordMsg[index].dir == MyEnum.Dir.vertical) {
//            kx = 1;
//            ky = 0;
//        }
//        int shakeRange = 2;
//        for (int j = 0; j < wordMsg[index].length; j++) {
//            int xx = tx + kx * j;
//            int yy = ty + ky * j;
//
////			letter[x][y].addAction(Actions.sequence(Actions.moveTo(geziGroup[x][y].getX() - diry * shakeRange, geziGroup[x][y].getY() + dirx * shakeRange, 0.1f, Interpolation.swingOut)
////					,Actions.moveTo(geziGroup[x][y].getX() + diry * shakeRange, geziGroup[x][y].getY() - dirx * shakeRange, 0.1f, Interpolation.swingOut)
////					,Actions.moveTo(geziGroup[x][y].getX(), geziGroup[x][y].getY(), 0.1f)));
//
//            float deltaX = -ky * shakeRange;
//            float deltaY = kx * shakeRange;
//            letter[xx][yy].addAction(Actions.sequence(Actions.moveTo(geziGroup[xx][yy].getX() + deltaX, geziGroup[xx][yy].getY() + deltaY, 0.05f)
//                    , Actions.repeat(2, Actions.sequence(Actions.moveTo(geziGroup[xx][yy].getX() - 2 * deltaX, geziGroup[xx][yy].getY() - 2 * deltaY, 0.05f), Actions.moveTo(geziGroup[xx][yy].getX() + 2 * deltaX, geziGroup[xx][yy].getY() + 2 * deltaY, 0.05f)))
//                    , Actions.moveTo(geziGroup[xx][yy].getX(), geziGroup[xx][yy].getY(), 0.2f)));
//        }
//
//    }
//
//    //从单词获得格子信息，传入是第几个单词
//    public int[] wordToGrid(int index) {
//        if (index < 0 || index > wordCount) {
//            return null;
//        }
//        int tx = wordMsg[index].x, ty = wordMsg[index].y;
//        if (index >= wordMsg.length) {
//            return null;
//        }
//        int pos[] = new int[wordMsg[index].length * 2];
//        int cnt = 0;
//        int kx = 0, ky = 0;
//        //等单词为水平方向的时候
//        if (wordMsg[index].dir == MyEnum.Dir.horizontal) {
//            kx = 0;
//            ky = 1;
//        } else if (wordMsg[index].dir == MyEnum.Dir.vertical) {
//            kx = 1;
//            ky = 0;
//        }
//        for (int j = 0; j < wordMsg[index].length; j++) {
//            pos[cnt++] = tx + kx * j;
//            pos[cnt] = ty + ky * j;
//            cnt++;
//        }
//
//        return pos;
//    }
//
//    //从单词获得格子信息，传入单词
//    public int[] wordToGrid(String searchWord) {
//        int index = 0;
//        for (int i = 0; i < wordCount; i++) {
//            if (searchWord.equals(wordMsg[i].str)) {
//                index = i;
//                break;
//            }
//        }
//
//        int tx = wordMsg[index].x, ty = wordMsg[index].y;
//
//        int pos[] = new int[wordMsg[index].length * 2];
//        int cnt = 0;
//        int kx = 0, ky = 0;
//        //等单词为水平方向的时候
//        if (wordMsg[index].dir == MyEnum.Dir.horizontal) {
//            kx = 0;
//            ky = 1;
//        } else if (wordMsg[index].dir == MyEnum.Dir.vertical) {
//            kx = 1;
//            ky = 0;
//        }
//        for (int j = 0; j < wordMsg[index].length; j++) {
//            pos[cnt++] = tx + kx * j;
//            pos[cnt] = ty + ky * j;
//            cnt++;
//        }
//
//        return pos;
//    }
//
//    private void resetGemstone(float x,float y){
//        if(feng == null) return;
//        if(isGameWin()){
//            feng.setVisible(false);
//            return;
//        }
//        feng.reset();
//        feng.clearActions();
//        feng.setVisible(false);
//        feng.setScale(gemScale * 0.5F);
//        feng.addAction(Actions.sequence(Actions.delay(0.1F),Actions.visible(true),Actions.scaleTo(gemScale,gemScale,0.3F,Interpolation.swingOut)));
//        feng.setPosition(x,y);
//    }
//
//    //获得空的格子，每日挑战
//    void getEmptyGrid(int nearby) {
//
//        if (gameMode == MyEnum.GameMode.normalMode) {
//            return;
//        }
//        int size = notPassSet.size();
//        if (size <= 0) {
//            feng.setVisible(false);
//            if (gameMode == MyEnum.GameMode.dailyMode) {
//                Prefs.putString("dailyPosition", GameData.json.prettyPrint(new Position(-1, -1, -1, -1)));
//                Prefs.flush();
//            }
//            return;
//        }
//        if (fengPosition.x >= 0 && fengPosition.y >= 0) {
//            if (gridMsg[fengPosition.x][fengPosition.y].appear == MyEnum.WordAppear.notAppear) {
//                return;
//            }
//        }
//        int tx, ty, k, p;
//        if (nearby != -1 && !isFind(nearby)) {
//            k = MyEnum.dirToInt(wordMsg[nearby].dir);
//            for (p = hintIndex[nearby]; p < hintLength[nearby]; p++) {
//                if (gridMsg[wordMsg[nearby].x + k * p][wordMsg[nearby].y + (1 - k) * p].appear == MyEnum.WordAppear.notAppear) {
//                    break;
//                }
//
//            }
//
//            tx = wordMsg[nearby].x + k * p;
//            ty = wordMsg[nearby].y + (1 - k) * p;
//            resetGemstone(geziGroup[tx][ty].getX() + halfGeziSizeX - feng.getWidth() / 2, geziGroup[tx][ty].getY() + halfGeziSizeY - feng.getHeight() / 2);
//            if (gridMsg[tx][ty].hIndex == nearby) {
//                fengPosition.set(tx, ty, nearby, gridMsg[tx][ty].vIndex);
//            } else {
//                fengPosition.set(tx, ty, nearby, gridMsg[tx][ty].hIndex);
//            }
//            if (gameMode == MyEnum.GameMode.dailyMode) {
//                Prefs.putString("dailyPosition", GameData.json.prettyPrint(fengPosition));
//                Prefs.flush();
//            }
//
//            return;
//        } else {
//            //选出枫叶起始位置
//            int t = 0;
//            if (!gameData.isYindaoed[14] && wordState == 0) {
//                //当每日挑战没引导的时候选择最上面的单词
//                int topH = Constants.MAX_INTEGER;
//                for (int i = 0; i < wordMsg.length; i++) {
//                    if (wordMsg[i].dir == MyEnum.Dir.horizontal) {
//                        if (topH > wordMsg[i].x) {
//                            t = i;
//                            topH = wordMsg[i].x;
//                        }
//                    }
//                }
//            } else {
//                //找到下面一个有格子的过去
//                boolean find = false;
//                //增加查找随机性，不是固定从第一个词找
//                int r = (int) (Math.random() * wordCount);
//                int turn = 0;
//                for (int z = r; turn < 2 && !find; z++) {
//                    int i = z % wordCount;
//                    if (i == r) {
//                        turn++;
//                    }
//                    if (!isFind(i)) {
//                        int dir = MyEnum.dirToInt(wordMsg[i].dir);
//                        for (int j = 0; j < wordMsg[i].length; j++) {
//                            if (gridMsg[wordMsg[i].x + dir * j][wordMsg[i].y + (1 - dir) * j].appear == MyEnum.WordAppear.appear) {
//                                find = true;
//                                t = i;
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            k = MyEnum.dirToInt(wordMsg[t].dir);
//            for (p = hintIndex[t]; p < hintLength[t]; p++) {
//                if (gridMsg[wordMsg[t].x + k * p][wordMsg[t].y + (1 - k) * p].appear == MyEnum.WordAppear.notAppear)
//                    break;
//            }
//
//            tx = wordMsg[t].x + k * p;
//            ty = wordMsg[t].y + (1 - k) * p;
//
//            if (gridMsg[tx][ty].hIndex == t) {
//                if (wordState == 0) {
//                    gridMsg[tx][ty].appear = MyEnum.WordAppear.hint;
//                    gameData.hintAppear[tx][ty] = true;
//                    if (gameMode == MyEnum.GameMode.dailyMode) {
//
//                        //没有单词，即只有第一次操作才会瞬移一格
//                        Prefs.putBoolean(hintAppearStr + tx + "_" + ty, true);
//
//                    }
//
//                    letter[tx][ty].setZIndex(geziGroup[tx][ty].getZIndex() + 1);
//                    letter[tx][ty].setVisible(true);
//                    fengPosition.set(tx, ty + 1, t, gridMsg[tx][ty + 1].vIndex);
//                    resetGemstone(geziGroup[tx][ty + 1].getX() + halfGeziSizeX - feng.getWidth() / 2, geziGroup[tx][ty + 1].getY() + halfGeziSizeY - feng.getHeight() / 2);
//                } else {
//                    fengPosition.set(tx, ty, t, gridMsg[tx][ty].vIndex);
//                    resetGemstone(geziGroup[tx][ty].getX() + halfGeziSizeX - feng.getWidth() / 2, geziGroup[tx][ty].getY() + halfGeziSizeY - feng.getHeight() / 2);
//                }
//
//            } else {
//                if (wordState == 0) {
//                    gridMsg[tx][ty].appear = MyEnum.WordAppear.hint;
//                    gameData.hintAppear[tx][ty] = true;
//                    if (gameMode == MyEnum.GameMode.dailyMode) {
//                        Prefs.putBoolean(hintAppearStr + tx + "_" + ty, true);
//                    }
//                    letter[tx][ty].setZIndex(geziGroup[tx][ty].getZIndex() + 1);
//                    letter[tx][ty].setVisible(true);
//                    fengPosition.set(tx + 1, ty, t, gridMsg[tx + 1][ty].hIndex);
//                    resetGemstone(geziGroup[tx + 1][ty].getX() + halfGeziSizeX - feng.getWidth() / 2, geziGroup[tx + 1][ty].getY() + halfGeziSizeY - feng.getHeight() / 2);
//                } else {
//                    fengPosition.set(tx, ty, t, gridMsg[tx][ty].hIndex);
//                    if(feng!=null && geziGroup[tx][ty]!=null) {
//                        resetGemstone(geziGroup[tx][ty].getX() + halfGeziSizeX - feng.getWidth() / 2, geziGroup[tx][ty].getY() + halfGeziSizeY - feng.getHeight() / 2);
//                    }
//                }
//
//            }
//
//            if (gameMode == MyEnum.GameMode.dailyMode) {
//                Prefs.putString("dailyPosition", GameData.json.prettyPrint(fengPosition));
//            }
//            Prefs.flush();
//        }
//
//
//    }
//
//    //判断词是不是找到了，返回true即为找到
//    boolean isFind(int index) {
//        if ((wordState & (1 << index)) != 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean isBeeEnable(int index){
//        if(beePositions == null) return false;
//        if ((beeState & (1 << index)) == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    //引导模式下正常游戏checkWord，返回state状态,返回-1表示进入了逻辑判断
//    int checkYindaoWord(int i) {
//        if (gameMode != MyEnum.GameMode.normalMode) {
//            return 0;
//        }
//        if (!gameData.isYindaoed[0] && gameData.gameIs == 0) {
//            //如果是第一关并且引导还没使用的时候
//            if (i == 0) {
//                setWordState(i);
//                float flyTime = setWordFly(i);
//                int index = 1;
//                String useWord = wordMsg[index].str;
//                yindaoWordIndex = index;
//
//                yindaoShou.setVisible(false);
//                for (int j = 0; j < yindaoXuxian.length; j++) {
//                    if (yindaoXuxian[j] != null)
//                        yindaoXuxian[j].setVisible(false);
//                }
//
//
//                //延迟时间 = 飞行时间加到单词全部显示出来的时间
//                //这里只考虑这个模式的那个出来
//                //todo
//
//
//                stage.addAction(Actions.sequence(Actions.delay(flyTime + 0.05f + lightGeziDelay * wordMsg[i].str.length() + linkGeziDelay)
//                        , Actions.run(()->{
//                            yindaoPanel.switchAction(0, 1, yindaoPanel.getStartPosY() + 20 - ViewportUtils.getDeltaY(), null, useWord);
//                            setWordZindex(0, yindaoMask.getZIndex());
//                            zimuShunxu = new int[useWord.length()];
//                            //判断拼装框下面的字母是否已经被使用过了
//                            boolean use[] = new boolean[letterLength];
//                            for (int j = 0; j < letterLength; j++) {
//                                use[j] = false;
//                            }
//                            for (int j = 0; j < zimuShunxu.length; j++) {
//                                for (int k = 0; k < letterLength; k++) {
//                                    //当字母相同的时候并且这个字母没有被使用的时候
//                                    if (useWord.charAt(j) == gameLetter.charAt(k) && !use[k]) {
//                                        use[k] = true;
//                                        zimuShunxu[j] = k;
//                                        break;
//                                    }
//                                }
//                            }
//                            setWordZindex(index, Constants.MAX_INTEGER);
//                            setYindaoActions();
//                        })));
//
//
//                return 1;
//            } else if (i == 1) {
//                if (wordState != 0) {
////					state = 1;
////					setWordAppear(i);
//                    stage.getRoot().clearActions();
//                    setWordState(i);
//                    setWordFly(i);
//                    for (int j = 0; j < yindaoXuxianCnt; j++) {
//                        yindaoXuxian[j].setVisible(false);
//                    }
//                    yindaoXuxianCnt = 0;
//                    yindaoShou.clearActions();
//                    yindaoShou.setVisible(false);
//                    yindaoMask.setVisible(false);
//                    yindaoPanel.clearActions();
//                    yindaoPanel.setVisible(false);
////					setWordState(i);
//                    return 1;
//                } else {
//                    return -1;
//                }
//            } else {
//                return -1;
//            }
//        } else {
//            return 0;
//        }
//    }
//
//
//    //引导模式下正常游戏checkWord，返回state状态
//    int checkDailyYindaoWord(int i) {
//        if (gameMode != MyEnum.GameMode.dailyMode) {
//            return 0;
//        }
//        if (!gameData.isYindaoed[14]) {
//            //如果是每日挑战的引导
//            if (i == yindaoWordIndex && !isFind(yindaoWordIndex)) {
//                canLianxian = false;
//                setWordState(i);
//                float flyTime = setWordFly(i);
//                feng.setZIndex(yindaoMask.getZIndex() - 1);
//
////				setWordState(i);
//                if (!isYindaoWithoutState) {
//                    saveWordState();
//                }
//                for (int j = 0; j < yindaoXuxianCnt; j++) {
//                    yindaoXuxian[j].setVisible(false);
//                }
//                yindaoXuxianCnt = 0;
//                yindaoShou.setVisible(false);
//                yindaoShou.clearActions();
//                pinGroup.setZIndex(yindaoMask.getZIndex() - 1);
//                float delayTime = flyTime + 0.05F + lightGeziDelay * wordMsg[i].str.length() + lightGeziDelay;
////                yindaoPanel.setVisible(false);
//                yindaoPanel.addAction(Actions.sequence(Actions.alpha(0,0.15F),Actions.visible(false)));
//                runDelay(()->{
//                    yindaoPanel.clearActions();
//                    yindaoMask.toFront();
//                    setWordZindex(fengPosition.index, Constants.MAX_INTEGER);
//                    yindaoPanel.toFront();
//                    feng.toFront();
//                    yindaoPanel.setScale(0.3F);
//                    yindaoPanel.getColor().a = 1;
//                    yindaoPanel.setVisible(true);
//                    yindaoPanel.updateYindao(24, 3, yindaoPanel.getStartPosY() -100 - ViewportUtils.getDeltaY() / 2,"", true);
//                    yindaoPanel.addAction(Actions.scaleTo(1,1,0.3F,Interpolation.swingOut));
//                    isWordYindao = false;
//                },delayTime + 0.7F);
//
//                return 1;
//            } else {
//                return -1;
//            }
//        } else {
//            return 0;
//        }
//    }
//
//
//    //划词是否找到正确单词
//    boolean isCheckWord = false;
//    //-1表示没有combo，其余按顺序来
//    String comboName;
//
//    void checkWord(int silentTime) {
//        //因为点击就会触发，这里防止误触，比如点击hint按钮之后会触发checkword，但是在收集模式里addComboTimes不该被checkWord打断
//        String string = stringBuilder.toString();
//        if (string.length() == 0) {
//            return;
//        }
//        addComboTimes = 0;
//        if (string.length() <= 1) {
//            preview.updateState(3);
//            myLine.alphaOut();
//            return;
//        }
//
//        //0没找到，1找到正常单词，2找到额外单词,3是重复找到常规单词，4是重复找到额外单词
//        int state = 0;
//        //找到的是第几个单词，用于重复处理的晃动
//        int matchWord = -1;
//        int s1, s3;
//        for (int i = 0; i < wordCount; i++) {
//            if (string.equals(gameAnswer[i])) {
//                matchWord = i;
//                //还没找到这个单词，连出这个单词
//                if (!isFind(i)) {
//                    s1 = checkYindaoWord(i);
//                    s3 = checkDailyYindaoWord(i);
//                    state = Math.max(state, s1);
//                    state = Math.max(state, s3);
//                    if (s1 == 0 && s3 == 0 && state == 0) {
//                        //非引导关，其他答对的情况
//                        PlatformManager.instance.wordCheck(string, "answer", gameData.DDNALevelText, levelType, (int) gameData.DDNAerrorBeforeFindWord, gameData.DDNAOperationInvalidWord);
//                        gameData.DDNAerrorBeforeFindWord = 0;
//                        state = 1;
//
//                        if (isNewLevel && !isYindaoWithoutState) {
//                            setWordState(i,true);
//                            if(isGameWin()){
//                                setInputProcessor(false);
//                            }
//                        }
//                        float time = setWordFly(i);
//                        try{
//                            baseGroup.addAction(Actions.delay(time + 0.2F,Actions.run(()->{
//                                if(shouldCheckJiesuan) {
//                                    goJiesuan();
//                                }
//                            })));
//                        }catch (Exception e){
//
//                        }
//                    }
//                } else {
//                    state = 3;
//                }
//
//                //只用执行1次
//                break;
//            }
//        }
//
//
//        if (!gameData.isYindaoed[0] && gameData.gameIs == 0 || (!gameData.isYindaoed[14] && gameMode == MyEnum.GameMode.dailyMode)) {
//            preview.updateState(state);
//            myLine.alphaOut();
//            return;
//        }
//
//
//        if(state !=1 && state!=3){
//            state = checkExtraWord(string);
//        }
//
//
//        if (myLine.getPointTotal() > 1) {
//            //0错误，1,2找到，3重复,4额外单词重复，5不能使用2词的额外词关卡找出2个字母的额外词
//            //字母效果
//            if (state == 0) {
//                if (string.length() >= 3) {
//                    isCheckWord = false;
//                    gameData.DDNAOperationInvalidWord++;
//                    gameData.DDNAErrorCount++;
//                    actualErrorTimes++;
//                    errorTimes++;
//                    beeDisappear();
//                    gameData.dailyErrorTime++;
//                    Prefs.putInteger("dailyErrorTime", gameData.dailyErrorTime);
//                    Prefs.flush();
//                    correctTimes = 0;
//
//                    if (gameData.canOutputMayFeelHard) {
//                        if (ddnaOperationInvalidWordCompareValue >= 0 && gameData.DDNAOperationInvalidWord > ddnaOperationInvalidWordCompareValue) {
//                            needToShowDNAPropRewardPanel = true;
//                            PlatformManager.instance.mayFeelHard(levelType, gameData.DDNALevelText, "freqError", silentTime, gameData.DDNAOperationInvalidWord);
//                            //输出数据
//                            gameData.canOutputMayFeelHard = false;
//                        }
//
//                    }
//                }
//
//                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORDINVALID_PATH, Sound.class));
//                for (int i = 0; i < letterIndex; i++) {
//
//                    bottomLettersGroup[letterIndexBuffer[i]].addAction(Actions.sequence(
//                            Actions.repeat(2, Actions.sequence(Actions.rotateTo(15, 0.1f), Actions.rotateTo(-15, 0.1f)))
//                            , Actions.parallel(Actions.rotateTo(0, 0.1f), Actions.scaleTo(1, 1, 0.1f))));
//                }
//
//                if (string.length() > 2) {
//                    correctTimes = 0;
//                    comboGroup.setVisible(false);
//                }
//
//
//            } else if (state == 1 || state == 2) {
//                needToShowDNAPropRewardPanel = false;
//                isCheckWord = true;
//                gameData.canOutputMayFeelHard = true;
//                if (gameData.DDNAOperationInvalidWord != 0) {
//                    gameData.DDNAOperationInvalidSaveWord.append(gameData.DDNAOperationInvalidWord + ",");
//
//                    gameData.DDNAOperationInvalidWord = 0;
//                }
//
//                actualErrorTimes = 0;
//                errorTimes = 0;
//                noTouchTime = 0;
//                for (int i = 0; i < letterIndex; i++) {
//                    bottomLettersGroup[letterIndexBuffer[i]].addAction(Actions.sequence(
//                            Actions.scaleTo(1.4f, 1.4f, 0.2f)
//                            , Actions.scaleTo(1f, 1f, 0.4f, Interpolation.fade)
//                    ));
//                }
//                if (state == 2) {
//                    gameData.DDNABonusCount++;
//                }
//            } else if (state == 3) {
//                isCheckWord = false;
//                gameData.DDNAOperationInvalidWord++;
//                errorTimes++;
//                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORDREPEAT_PATH, Sound.class), 0.6f);
//
//                for (int i = 0; i < letterIndex; i++) {
//                    bottomLettersGroup[letterIndexBuffer[i]].addAction(Actions.sequence(
//                            Actions.scaleTo(1.4f, 1.4f, 0.2f)
//                            , Actions.scaleTo(1f, 1f, 0.4f, Interpolation.fade)
//                    ));
//                }
//                if (matchWord >= 0) {
//                    setRepeatWordShake(matchWord);
//                }
//
//                if (gameData.canOutputMayFeelHard) {
//                    if (ddnaOperationInvalidWordCompareValue >= 0 && gameData.DDNAOperationInvalidWord > ddnaOperationInvalidWordCompareValue) {
//                        needToShowDNAPropRewardPanel = true;
//                        PlatformManager.instance.mayFeelHard(levelType, gameData.DDNALevelText, "freqError", silentTime, gameData.DDNAOperationInvalidWord);
//                        //输出数据
//                        gameData.canOutputMayFeelHard = false;
//                    }
//
//                }
//            } else if (state == 4) {
//                isCheckWord = false;
//                gameData.DDNAOperationInvalidWord++;
//                errorTimes++;
//                if (dictBtn != null) {
//                    showWarningGroup("You've already found this word!");
//                    float deltaX = random(-2, 2);
//                    float deltaY = random(-2, 2);
//                    dictBtn.addAction(Actions.sequence(Actions.moveBy(deltaX, deltaY, 0.05f), Actions.repeat(2, Actions.sequence(Actions.moveBy(-deltaX * 2, -deltaY * 2, 0.05f), Actions.moveBy(deltaX * 2, deltaY * 2, 0.05f))), Actions.moveBy(-deltaX, -deltaY, 0.05f)));
//
//                }
//
//                if (gameData.canOutputMayFeelHard) {
//                    if (ddnaOperationInvalidWordCompareValue >= 0 && gameData.DDNAOperationInvalidWord > ddnaOperationInvalidWordCompareValue) {
//                        needToShowDNAPropRewardPanel = true;
//                        PlatformManager.instance.mayFeelHard(levelType, gameData.DDNALevelText, "freqError", silentTime, gameData.DDNAOperationInvalidWord);
//                        //输出数据
//                        gameData.canOutputMayFeelHard = false;
//                    }
//
//                }
//            } else if (state == 5) {
//                showWarningGroup("No 2 letter words!");
//            }
//            preview.updateState(state);
//            myLine.alphaOut();
//
//
//            if (errorTimes == 2) {
//                boolean videoReady = PlatformManager.instance.isRewaedVideoReady();
//                if (gameMode == MyEnum.GameMode.normalMode && !gameData.isYindaoed[4] && gameSolved > 0 && (yindaoPanel != null && !yindaoPanel.isShowing) && videoReady) {
//                    errorTimes = 0;
//                    gameData.setYindao(4);
//
//
//                    if (gameData.dnaSetPropPanelLessFlag || gameData.dnaSetPropPanelMoreFlag) {
//
//                    } else {
//                        yindaoPanel.updateYindao(-1, 0, videoBtn.getTop() + 80,
//                                new float[]{videoBtn.getX() + videoBtn.getWidth() / 2 - 8, videoBtn.getTop() - 6, 0}, true);
//                        showPanel(yindaoPanel);
//                        videoBtn.setAnimation("2",true);
//                        videoBtn.setVisible(true);
//                        videoBtn.toFront();
//                        videoBtn.setTouchable(Touchable.enabled);
//                        videoBtn.setColor(1,1,1,1);
//                        videoBtnClosed = false;
//                    }
//
//
//                }
//            } else if (errorTimes >= Constants.errorPropRewardPanelTimes) {
//                //答错次数到达一定量展示看视频面板
//                if (gameData.coinNumber < Constants.propRewardPanelValue && gameData.errorGameTime >= Constants.errorPropRewardPanelGameTime && gameData.errorRemindNum > 0) {
//                    gameData.errorRemindNum--;
//                    Prefs.putInteger("errorRemindNum", gameData.errorRemindNum);
//                    Prefs.flush();
//                    errorTimes = 0;
////					showPanel(propRewardPanel);
//                }
//            }
//            checkShowVideoBox();
//        }
//
//
//        if (needToShowDNAPropRewardPanel) {
//            showDNAPropRewardPanel();
//        }
//
//
//    }
//
//
//    private int checkExtraWord(String string){
//        int state = 0;
//        for (int i = 0; i < extraWord.length; i++) {
//            if (string.equals(extraWord[i])) {
//                if (gameData.extraState.charAt(i) == '0') {
//                    if (extraWord[i].length() < 3) {
//                        //不能使用2词的额外词关卡找出2个字母的额外词
//                        state = 5;
//                    } else {
//                        state = 2;
//                        gameData.extraState.setCharAt(i, '1');
//
//                        if (!dictBtn.isVisible() && dicBtnCanVisible) {
//                            dictBtn.setVisible(true);
//                        }
//
//                        PlatformManager.instance.wordCheck(string, "bonus", gameData.DDNALevelText, levelType, (int) gameData.DDNAerrorBeforeFindWord, gameData.DDNAOperationInvalidWord);
//                        gameData.DDNAerrorBeforeFindWord = 0;
//                        //添加已经找到的额外单词
//                        gameData.findWord[gameData.findWordLength] = string;
//                        gameData.findWordLength++;
//                        gameData.findExtraWord[gameData.findExtraWordLength] = string;
//                        gameData.findExtraWordLength++;
//                        if((gameMode == MyEnum.GameMode.normalMode && isNewLevel) || gameMode == MyEnum.GameMode.dailyMode ) {
//                            Prefs.putString(extraStateStr, gameData.extraState.toString());
//                        }
//                        //那个面板添加单词
//
//                        allWordPanel.addWord(string, true);
//
//                        //这个extraWordsCount应该是通用的
////						gameData.extrawordSet.add(string);
//
//                        Prefs.putString("extraword" + gameData.findExtraWordNum, string);
//                        gameData.findExtraWordNum++;
//                        gameData.extraWordCount++;
//                        Prefs.putInteger("extraWordCount", gameData.extraWordCount);
//
//                        for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                            if (gameData.dailyQuestId[k0] == 7) {
//                                if (i == 2 && gameData.gameSolved < 85) {
//                                    break;
//                                }
//                                gameData.dailyextraWordCount++;
//                                Prefs.putInteger("dailyextraWordCount", gameData.dailyextraWordCount);
//                                break;
//                            }
//                        }
//
//
//                        showQuestWarningGroup("extraWordCount", 0);
//                        showQuestWarningGroup("dailyextraWordCount", 1);
//
//                        AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORD_PATH, Sound.class));
//
//                        preview.setVisible(false);
//
//                        for (int j = 0; j < extraWordGroupMaxn; j++) {
//                            if (!extraWordGroupUse[j]) {
//                                extraWordGroupUse[j] = true;
//                                extraWordGroup[j].setText(string);
//                                extraWordGroup[j].toFront();
//
//                                extraWordGroup[j].setWidth(extraWordGroup[j].getWidth());
//                                extraWordGroup[j].setOriginX(extraWordGroup[j].getWidth() / 2);
//
//
//                                final int t = j;
//                                extraWordGroup[j].setScale(1);
//                                extraWordGroup[j].setPosition(preview.getX(), preview.getY());
//                                extraWordGroup[j].setVisible(true);
//
//                                showExtraWordAnimation(j, t);
//
//                                break;
//                            }
//                        }
//
//                        //展示额外单词的引导
//                        if (!gameData.isYindaoed[5] && (yindaoPanel != null && !yindaoPanel.isShowing) && gameMode == MyEnum.GameMode.normalMode && isNewLevel) {
//                            gameData.setYindao(5);
//
//                            dictBtn.clearActions();
//                            dictBtn.setVisible(true);
//                            float x = dictBtn.getX();
//                            float y = dictBtn.getY();
//                            //350
//                            yindaoPanel.updateYindao(-1, 2, y - 490,
//                                    new float[]{x + dictBtn.getWidth() / 2, y + 20, 180}, true);
//                            showPanel(yindaoPanel);
//                            dictBtn.toFront();
//                        }
//
//                        //额外单词
//                        gameData.questWordSpllingNum++;
//                        Prefs.putInteger("questWordSpllingNum", gameData.questWordSpllingNum);
//
//                        for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                            if (gameData.dailyQuestId[k0] == 9) {
//                                if (k0 == 2 && gameData.gameSolved < 85) {
//                                    break;
//                                }
//                                gameData.dailyWordSpllingNum++;
//                                Prefs.putInteger("dailyWordSpllingNum", gameData.dailyWordSpllingNum);
//                                break;
//                            }
//                        }
//                        showQuestWarningGroup("questWordSpllingNum", 0);
//                        showQuestWarningGroup("dailyWordSpllingNum", 1);
//
//                        Prefs.flush();
//                    }
//                } else if (gameData.extraState.charAt(i) == '1') {
//                    //额外单词重复找到
//                    state = 4;
//                }
//
//                break;
//            }
//        }
//        return state;
//    }
//
//    private Array<WordMsg> wordMsgsArray;
//
//    private void checkShowVideoBox(){
//        if(gameMode != MyEnum.GameMode.normalMode) return;
//        if(gameIs<85) return;
//        if(!UserProbability.instance.VideoPush) return;
//        if(!GameData.instance.dnaSetOutput) return;
//        if(videoBoxShow) return;
//        if(errorTimes < 3) return;
//        if(wordMsgsArray == null){
//            wordMsgsArray = new Array<>();
//            wordMsgsArray.addAll(wordMsg);
//            wordMsgsArray.sort(new Comparator<WordMsg>() {
//                @Override
//                public int compare(WordMsg o1, WordMsg o2) {
//                    if(o1.length>o2.length){
//                        return -1;
//                    }else if(o1.length<o2.length){
//                        return 1;
//                    }else{
//                        return 0;
//                    }
//                }
//            });
//        }
//
//        Position temp = getShowboxPos(wordMsgsArray,true);
//        if(temp.x == -1){
//            temp = getShowboxPos(wordMsgsArray,false);
//        }
//        if(temp.x != -1){
//            showVideoBox(temp);
//        }
//    }
//
//    private Position getShowboxPos(Array<WordMsg> wordMsgsArray,boolean exportNode){
//        Position temp = new Position();
//
//        for(int i = 0;i<wordMsgsArray.size && temp.x == -1;i++) {
//            WordMsg msg = wordMsgsArray.get(i);
//            if (msg.dir == MyEnum.Dir.horizontal) {
//                for(int q = 0;q<msg.length;q++){
//                    int tx = msg.x;
//                    int useY = msg.y + q;
//                    if( gridMsg[tx][useY].appear == MyEnum.WordAppear.notAppear && coinGeziImages[tx][useY] == null){
//                        if(exportNode){
//                            if(gridMsg[tx][useY].vIndex==-1) {
//                                temp.set(tx, useY);
//                                break;
//                            }
//                        }else{
//                            temp.set(tx,useY);
//                            break;
//                        }
//                    }
//                }
//            }else{
//                for(int q = 0;q<msg.length;q++){
//                    int tx = msg.x + q;
//                    int useY = msg.y;
//                    if( gridMsg[tx][useY].appear == MyEnum.WordAppear.notAppear && coinGeziImages[tx][useY] == null){
//                        if(exportNode){
//                            if(gridMsg[tx][useY].hIndex == -1){
//                                temp.set(tx,useY);
//                                break;
//                            }
//                        }else {
//                            temp.set(tx, useY);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        return temp;
//    }
//
//    //给连击次数增加addTimes次同时展示连击图片
//    void showCombo() {
//        if (addComboTimes == 0) {
//            return;
//        }
//        AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORD_PATH, Sound.class));
//        comboName = "";
//        correctTimes += addComboTimes;
//        addComboTimes = 0;
//        errorTimes = 0;
//        if(correctTimes>=2 && gameMode == MyEnum.GameMode.dailyMode){
//            addDailyJindu();
//            dailyProgress.setProgress(dailyJindu);
//        }
//        if (correctTimes == 2) {
//            gameData.goodCombosNum++;
//            Prefs.putInteger("goodCombosNum", gameData.goodCombosNum);
//
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 0) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailygoodCombosNum++;
//                    Prefs.putInteger("dailygoodCombosNum", gameData.dailygoodCombosNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("goodCombosNum", 0);
//            showQuestWarningGroup("dailygoodCombosNum", 1);
//            comboName = "good";
//        } else if (correctTimes == 3) {
//            gameData.greatCombosNum++;
//            Prefs.putInteger("greatCombosNum", gameData.greatCombosNum);
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 1) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailygreatCombosNum++;
//                    Prefs.putInteger("dailygreatCombosNum", gameData.dailygreatCombosNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("greatCombosNum", 0);
//            showQuestWarningGroup("dailygreatCombosNum", 1);
//            comboName = "great";
//        } else if (correctTimes == 4) {
//            gameData.excellentCombosNum++;
//            Prefs.putInteger("excellentCombosNum", gameData.excellentCombosNum);
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 2) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailyexcellentCombosNum++;
//                    Prefs.putInteger("dailyexcellentCombosNum", gameData.dailyexcellentCombosNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("excellentCombosNum", 0);
//            showQuestWarningGroup("dailyexcellentCombosNum", 1);
//            comboName = "excellent";
//        } else if (correctTimes == 5) {
//            gameData.amazingCombosNum++;
//            Prefs.putInteger("amazingCombosNum", gameData.amazingCombosNum);
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 3) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailyamazingCombosNum++;
//                    Prefs.putInteger("dailyamazingCombosNum", gameData.dailyamazingCombosNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("amazingCombosNum", 0);
//            showQuestWarningGroup("dailyamazingCombosNum", 1);
//            comboName = "amazing";
//        } else if (correctTimes == 6) {
//            gameData.unbelievableCombosNum++;
//            Prefs.putInteger("unbelievableCombosNum", gameData.unbelievableCombosNum);
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 4) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailyunbelievableCombosNum++;
//                    Prefs.putInteger("dailyunbelievableCombosNum", gameData.dailyunbelievableCombosNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("unbelievableCombosNum", 0);
//            showQuestWarningGroup("dailyunbelievableCombosNum", 1);
//            comboName = "unbelievable";
//        } else if (correctTimes >= 7) {
//            gameData.outstandingCombosNum++;
//            Prefs.putInteger("outstandingCombosNum", gameData.outstandingCombosNum);
//            for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//                if (gameData.dailyQuestId[k0] == 5) {
//                    if (k0 == 2 && gameData.gameSolved < 85) {
//                        break;
//                    }
//                    gameData.dailyoutstandingCombosNum++;
//                    Prefs.putInteger("dailyoutstandingCombosNum", gameData.dailyoutstandingCombosNum);
//                    break;
//                }
//            }
//            Prefs.flush();
//            showQuestWarningGroup("outstandingCombosNum", 0);
//            showQuestWarningGroup("dailyoutstandingCombosNum", 1);
//            comboName = "outstanding";
//        }
////		Prefs.flush();
//
//        //显示连击图
//
//        if (correctTimes >= 2) {
//            guliciSpineGroup.toFront();
//            guliciSpineGroup.setVisible(true);
//            guliciSpineGroup.setAnimation(comboName, false);
//
//
//            //当不是第一关引导的时候才展示连击动画
//            //当单词飞上去不是瞬间出来combo，当应该出来的时候如果打开其他商店界面等不应该展示这个
//            if (gameData.isYindaoed[0] && geziState == true && correctTimes >= 4) {
//                comboGroup.setVisible(true);
//
//
//            }
//
//        }else if(emptyBox.size() == 0){
//            comboName = "good";
//            guliciSpineGroup.toFront();
//            guliciSpineGroup.setVisible(true);
//            guliciSpineGroup.setAnimation(comboName, false);
//            //当不是第一关引导的时候才展示连击动画
//            //当单词飞上去不是瞬间出来combo，当应该出来的时候如果打开其他商店界面等不应该展示这个
//            if (gameData.isYindaoed[0] && geziState == true && correctTimes >= 4) {
//                comboGroup.setVisible(true);
//            }
//        }
//        if(emptyBox.size() == 0 && GameData.instance.isGoodPhone){
//            YanhuaGroup group = new YanhuaGroup();
//            Vector2 vector2 = new Vector2(0,0);
//            baseGroup.localToStageCoordinates(vector2);
//            group.setPosition(vector2.x,vector2.y);
//            stage.addActor(group);
//        }
//
//    }
//
//    //展示找到额外单词的动画
//    void showExtraWordAnimation(int j, int t) {
//        extraWordGroup[j].addAction(Actions.sequence(
//                Actions.scaleTo(1.3f, 1.3f, 0.2f)
//                , Actions.parallel(Actions.moveToAligned(dictBtn.getX() + dictBtn.getWidth() / 2, dictBtn.getY() + 60, Align.top, 0.75f, Interpolation.pow5Out)
//                        , Actions.scaleTo(0.1f, 0.1f, 0.75f))
//                , Actions.visible(false)
//                , Actions.run(()->{
//                    extraWordGroup[t].setScale(1);
//                    if((gameMode == MyEnum.GameMode.normalMode && isNewLevel)) {
//                        for (int i = 0; i < extraWordGroupMaxn; i++) {
//                            if (!extraWordJinbiSpineGroupUse[i]) {
//                                extraWordJinbiSpineGroupUse[i] = true;
//                                extraWordJinbiSpineGroup[i].setVisible(true);
//
//                                float startX, startY, endX, endY;
//                                startX = dictBtn.getX() + dictBtn.getWidth() / 2;
//                                startY = coinGroup.coinImageY;
//
//                                endX = coinGroup.coinImageX;
//                                endY = coinGroup.coinImageY;
//
//                                extraWordJinbiSpineGroup[i].setPosition(startX, startY, Align.center);
//                                extraWordJinbiSpineGroup[i].toFront();
//                                extraWordJinbiSpineGroup[i].setAnimation("1", false);
//
//
//                                BezierMoveAction bezierMoveAction = new BezierMoveAction();
//                                bezierMoveAction.setBezier(startX, startY, (startX + endX) / 2, (startY + endY) / 2 - 50, (startX + endX) / 2, (startY + endY) / 2 - 50, endX, endY);
//                                bezierMoveAction.setDuration(0.2f);
//
//
//                                extraWordJinbiSpineGroup[i].addAction(Actions.sequence(
//                                        bezierMoveAction
//                                        , Actions.run(() -> {
//                                            gameData.coinBuffer = 1;
//                                            extraWordJinbiSpineGroupUse[t] = false;
//                                        })
//                                        , Actions.visible(false)
//                                ));
//
//                                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_COINCLAIM_PATH, Sound.class));
//                                break;
//                            }
//                        }
//                    }
//                    extraWordGroupUse[t] = false;
//                })));
//    }
//
//
//
//    //判断游戏是否获胜，传入当前点亮的格子，需要是空的来判断
//    boolean judgeGameWin(int emptyGeziNum) {
//        if (emptyBox.size() == emptyGeziNum) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    private boolean isGameWin(){
//        if(emptyBox.size() == 0 || passWordState == wordState) return true;
//        return false;
//    }
//
//    private void checkQuickFinish(long endTime){
//        int wordCount = GameData.instance.findWordLength - GameData.instance.findExtraWordLength;
//        int timeUsed = (int)((endTime - startTime)/1000);
//        GameData.instance.quickFinish = false;
//        if(gameIs>=80){
//            if(wordCount<=10){
//                if(timeUsed<=30) {
//                    GameData.instance.quickFinish = true;
//                }
//            }else if(wordCount<=15){
//                if(timeUsed<=45) {
//                    GameData.instance.quickFinish = true;
//                }
//            }else if(wordCount<=20){
//                if(timeUsed<= 60) {
//                    GameData.instance.quickFinish = true;
//                }
//            }else{
//                GameData.instance.quickFinish = false;
//            }
//        }
//    }
//
//    void goJiesuan() {
//        Prefs.putString(extraStateStr, "");
//        if ((emptyBox.size() == 0 || (passWordState == wordState && isFly == 0)) && !isJiesuan) {
//            endTime = System.currentTimeMillis();
//            checkQuickFinish(endTime);
//            isJiesuan = true;
//            //获胜的改到这里输出
//            //输出levelExit
//            if (gameMode == MyEnum.GameMode.dailyMode || gameData.dailyPass) {
//                PlatformManager.instance.levelExit("daily", gameData.DDNALevelText, gameData.DDNADailyStartRecord, 1,
//                        gameData.DDNAErrorCount, gameData.DDNATimeCount, gameData.DDNABonusCount, gameData.coinNumber, gameData.allFengNum, true, DDNALeverVersion,"null",0,0);
//            }else {
//                if (gameData.isNewLevel) {
//                    PlatformManager.instance.levelExit("normal", gameData.DDNALevelText, gameData.DDNAStartRecord, 1,
//                            gameData.DDNAErrorCount, gameData.DDNATimeCount, gameData.DDNABonusCount, gameData.coinNumber, gameData.allFengNum, gameData.DDNAGameHasReward, DDNALeverVersion,beePositions!=null?"spellingBee":"null",maxBees,levelBees);
//                    gameData.DDNAStartRecord = 0;
//                    Prefs.putInteger("DDNAStartRecord", 0);
//                    Prefs.flush();
//                } else {
//                    PlatformManager.instance.levelExit("normal", gameData.DDNALevelText, 1, 1,
//                            gameData.DDNAErrorCount, gameData.DDNATimeCount, gameData.DDNABonusCount, gameData.coinNumber, gameData.allFengNum, false, DDNALeverVersion,"null",0,0);
//                }
//                RuntimeData.instance.gameTime = gameData.DDNATimeCount;
//            }
//            if (gameData.DDNAOperationIntervalSaveTime.length() != 0) {
//                Prefs.putInteger("changeMayFeelHardSaveIndex", gameData.changeMayFeelHardSaveIndex);
//                Prefs.putString("DDNAOperationIntervalSaveTime" + gameData.changeMayFeelHardSaveIndex, gameData.DDNAOperationIntervalSaveTime.toString());
//                Prefs.flush();
//            }
//
//            if (gameData.DDNAOperationInvalidSaveWord.length() != 0) {
//                Prefs.putInteger("changeMayFeelHardSaveIndex", gameData.changeMayFeelHardSaveIndex);
//                Prefs.putString("DDNAOperationInvalidSaveWord" + gameData.changeMayFeelHardSaveIndex, gameData.DDNAOperationInvalidSaveWord.toString());
//                Prefs.flush();
//            }
//
//            if (isNewLevel && gameMode == MyEnum.GameMode.normalMode) {
//                //如果是第一关同时引导还没有完成的时候
//                if (gameData.gameIs == 0 && !gameData.isYindaoed[0]) {
//                    gameData.setYindao(0);
//                }
//
//                gameData.gameSolved = gameIs + 1;
//                Prefs.putInteger("gameSolved", gameData.gameSolved);
//
//                gameData.chapterSolved = ChapterUtil.getLevelChapterId(gameData.gameSolved);
//                gameData.nextChapterBgName = gameData.getGameplayBgPath(gameData.chapterSolved + 1);
//                ChapterUtil.updateBigChapterId();
//
//                if (gameData.dailyQuestId[0] == 6 || gameData.dailyQuestId[1] == 6 || (gameData.dailyQuestId[2] == 6 && gameData.gameSolved >= 85)) {
//                    //当是第三个任务时85关后才开始计算每日通过
//                    gameData.dailygameSolved++;
//                    Prefs.putInteger("dailygameSolved", gameData.dailygameSolved);
//                }
//                Prefs.putInteger("levelBees",0);
//                Prefs.putInteger(stateStr, 0);
//                Prefs.putInteger("beeState",0);
//                clearCurrentData();
//            }
//            gameEndBtnHide();
//
//            if (gameMode != MyEnum.GameMode.normalMode) {
//
//                runDelay(()->{
//                    dailyPanel.setGameEnd(true,gameData.selectDay);
//                    showPanel(dailyPanel);
//                }, linkGeziDelay + 0.5f);
//
//            } else {
//                runDelay(() -> {
//                    showPanel(levelPassPanel);
//                    showQuestWarningGroup("gameSolved", 0);
//                    showQuestWarningGroup("dailygameSolved", 1);
//                }, linkGeziDelay + 0.5f);
//            }
//        }
//    }
//
//
//    //让单词不用等待出现,比如用于单词飞上去
//    void addActionOnLetter(MyEnum.Dir dir, int xx, int yy, Actor actor, boolean isHint, int flyIndex) {
//        addActionOnLetter(dir, xx, yy, 0, actor, false, isHint, flyIndex);
//    }
//
//    float letterScaleV1 = 0.8f, letterScaleV2 = 1.06f;
//
//    //让单词是否出现，link表示是否连接
//    void addActionOnLetter(MyEnum.Dir dir, int xx, int yy, float delta, Actor actor, boolean link, boolean isHint, int flyIndex) {
//        if (actor == null) {
//            return;
//        }
//        noTouchTime = 0;
//        tPosition.set(xx, yy, gridMsg[xx][yy].hIndex, gridMsg[xx][yy].vIndex);
//        emptyBox.remove(tPosition);
//
//
//        runDelay(()->{
//            GetcoinWord(xx, yy);
//        }, delta);
//
//
//        //这个不知道删除了会有什么影响
////		if (actor.getActions().size != 0){
////			return;
////		}
//        actor.clearActions();
//        actor.setScale(1.0f);
//
//
//        if (actor.getName().equals("letter")) {
//            if (flyIndex != -1 && !actor.isVisible()) {
////				flyLbl[flyIndex].setVisible(false);
//                letter[xx][yy].setZIndex(geziGroup[xx][yy].getZIndex() + 1);
//                letter[xx][yy].setVisible(true);
////				actor.addAction(Actions.sequence(Actions.delay(delta)
////						, Actions.scaleTo(1f, 1f)
////						, Actions.scaleTo(1, 1, 3 * Constants.AnimationGap)
////				));
//            } else {
//                actor.setVisible(true);
//            }
//            actor.setZIndex(geziGroup[xx][yy].getZIndex() + 1);
//        } else if (actor.getName().equals("geziGroup")) {
//            //给格子增加动作，能连起来的时候
//            actor.addAction(Actions.sequence(
//                    Actions.delay(delta),
//                    Actions.scaleTo(1, 1)
//                    , Actions.scaleTo(letterScaleV1, letterScaleV1, 6 * Constants.AnimationGap, Interpolation.pow2In)
//                    , Actions.run(()->{
//                        if (geziImages[xx][yy].isVisible() && !isHint) {
//                            skinImages[xx][yy].addAction(Actions.sequence(Actions.alpha(0), Actions.visible(true), Actions.alpha(1, 10 * Constants.AnimationGap)));
////								skinImages[xx][yy].setVisible(true);
//                            skinImages[xx][yy].setVisible(true);
//                            geziImages[xx][yy].setVisible(false);
//                            letter[xx][yy].setZIndex(geziGroup[xx][yy].getZIndex() + 1);
//                        }
//                    })
//                    , Actions.scaleTo(letterScaleV2, letterScaleV2, 6 * Constants.AnimationGap, Interpolation.pow3Out)
////					, Actions.delay(Constants.AnimationGap)
//                    , Actions.scaleTo(1, 1, 4 * Constants.AnimationGap)));
//
//            //格子和字母动作一样
//            letter[xx][yy].clearActions();
//            letter[xx][yy].setZIndex(geziGroup[xx][yy].getZIndex() + 1);
//            letter[xx][yy].setScale(1);
//            letter[xx][yy].setVisible(true);
//            letter[xx][yy].addAction(Actions.sequence(
//                    Actions.delay(delta),
//                    Actions.scaleTo(letterScaleV1, letterScaleV1, 6 * Constants.AnimationGap, Interpolation.pow2In)
//                    , Actions.run(()->{
//                        if (isSkinAlwaysBlack) {
//                            letterLbl[xx][yy].setColor(Color.BLACK);
//                        } else {
//                            letterLbl[xx][yy].setColor(Color.WHITE);
//                        }
//                    })
//                    , Actions.scaleTo(letterScaleV2, letterScaleV2, 6 * Constants.AnimationGap, Interpolation.pow3Out)
////					, Actions.delay(Constants.AnimationGap)
//                    , Actions.scaleTo(1, 1, 4 * Constants.AnimationGap)));
//
//
//        }
//
//
//        actionActor.addAction(Actions.sequence(Actions.delay(delta), Actions.run(()->{
//            goJiesuan();
//        })));
//
//
//    }
//
//
//    final int flyMaxn = 30;
//    boolean[] useFly;
//    Label[] flyLbl;
//    Group[] flyLblGroup;
//    int[] flyShunxu;
//    Label.LabelStyle flyLabelStyle;
//
//    void initFlyLbl() {
//        BitmapFont bitmapFont = Assets.getInstance().assetManager.get("ui/gameplayNew/font/Arial-BoldMT_70_1.fnt", BitmapFont.class);
//        if (isSkinAlwaysBlack) {
//            flyLabelStyle = new Label.LabelStyle(bitmapFont, Color.BLACK);
//        }else{
//            flyLabelStyle = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());
//        }
//        useFly = new boolean[flyMaxn];
//        flyLbl = new Label[flyMaxn];
//        flyLblGroup = new Group[flyMaxn];
//
//        flyShunxu = new int[7];
//        for (int i = 0; i < 7; i++) {
//            flyShunxu[i] = -1;
//        }
//        for (int i = 0; i < flyMaxn; i++) {
//            useFly[i] = false;
//            flyLbl[i] = new Label("", flyLabelStyle);
//            flyLbl[i].setAlignment(Align.bottomLeft);
//            flyLbl[i].setFontScale(40f/70);
//            flyLblGroup[i] = new Group();
//            flyLblGroup[i].addActor(flyLbl[i]);
//            stage.addActor(flyLblGroup[i]);
//        }
//    }
//
//    Preview preview;
//
//    void initPreview() {
//        preview = new Preview(skinColor, bujuLabelStyle, bujuFontSize);
//        stage.addActor(preview);
//    }
//
//    Image[][] geziImages, coinGeziImages, skinImages;
//    Image[][] fingerImages;
//    //A是黑色，B是白色
//    SpineGroup[][] zmcxSpineGroup, zmcxASpineGroup, zmcxBSpineGroup, gegSpineGroup;
//    SpineGroup[] fastHintSpineGroup,fastHintSpineGroup2;
//    SpineGroup fastHintCloudSpineGroup;
//    float GEZI_MIDDLE_Y;
//    Group bujuGroup;
//    float sy, ey, sx, ex, mx, my, dx, dy;
//    //实际上格子的ey
//    float actualEy;
//    Group[][] geziGroup;
//    Vector2[][] geziPos;
//    Group[][] letter;
//    //上面格子的字母
//    Label[][] letterLbl;
//
//    Gemstone feng;
//    //上面字母的偏移
//    float offsetX[], offsetY[];
//    float bujuFontScale;
//
//    Label.LabelStyle bujuLabelStyle;
//    //把上面格子上面单词颜色单独拎出来
//    Label.LabelStyle letterLableStyle;
//    float bujuFontSize;
//    SnapshotArray<Actor> bujuList;
//
//    void initBuju() {
//        videoBox = new SpineGroup(Assets.getInstance().assetManager.get(Constants.spineVideoBoxPath), new AnimationState.AnimationStateAdapter(){
//            @Override
//            public void complete(AnimationState.TrackEntry entry) {
//                if (videoBox.getAnimationName().endsWith("animation1")) {
//                    videoBox.setAnimation("animation2");
//                }else if(videoBox.getAnimationName().endsWith("animation3")){
//                    videoBox.remove();
//                }
//            }
//        });
//        if(useWhiterPin){
//            videoBox.setSkin("baise");
//        }else{
//            videoBox.setSkin("heise");
//        }
//        videoBox.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                gameData.rewardVideoState = MyEnum.RewardVideoState.VideoGameBoxState;
//                PlatformManager.instance.showRewardedVideoAds();
//            }
//        });
//        videoBox.setSize(130,134);
//        videoBox.setOrigin(Align.center);
//        videoBox.getSpineActor().setPosition(videoBox.getWidth()/2,videoBox.getHeight()/2);
//        int count = wordCount;
//        GEZI_MIDDLE_Y = 944f;
//
//        hintId = 0;
//        isFound = new boolean[count + 5];
//        hintIndex = new int[count + 5];
//        hintLength = new int[count + 5];
//
//        for (int i = 0; i < count; i++) {
//            isFound[i] = false;
//            hintIndex[i] = 0;
//            hintLength[i] = gameAnswer[i].length();
//        }
//
//
//        if (gameData.gameSolved == 0) {
//            sy = 1190 + ViewportUtils.getDeltaY();
////				ey = Math.max(640 - ViewportUtils.getDeltaY() - 67, yindaoPanel.getTopInCalculation(yindaoPanel.getStartPosY() + 20 - ViewportUtils.getDeltaY()));
//
////				ey = Math.max(710 - ViewportUtils.getDdeltaY(), 640 - ViewportUtils.getDeltaY() - 67);
//            ey = Math.max(710 - ViewportUtils.getDdeltaY(), 573 - ViewportUtils.getDeltaY());
////				ey = 640 - ViewportUtils.getDeltaY() - 67;
//        } else if (gameData.gameSolved < Constants.fisrtShowInterstitial) {
//            sy = 1190 + ViewportUtils.getDeltaY();
//            ey = 573 - ViewportUtils.getDeltaY();
//        } else {
//            sy = 1190 + ViewportUtils.getDeltaY();
//            ey = 640 - ViewportUtils.getDeltaY() / 2;
//        }
//
//        sx = ViewportUtils.getLeft() + 20;
//        ex = ViewportUtils.getRight() - 20;
//
//        bujuList = new SnapshotArray<>();
//        bujuGroup = new Group();
//        bujuGroup.setName("bujuGroup");
//        bujuGroup.setSize(ex - sx, sy - ey);
//        bujuGroup.setPosition(sx, ey);
//        bujuGroup.setOrigin(Align.center);
//        stage.addActor(bujuGroup);
//
//        //格子水平的距离和纵向的距离
//
//        dx = 6;
//        dy = 4;
//
//        mx = (sx + ex) / 2;
//        my = (sy + ey) / 2;
//
//
//        float radio = 130/134F;
//        geziSizeY = Math.min((ex - sx - (w + 1) * dx) / w, (sy - ey - (h + 1) * dy) / h);
//
//        if (gameData.gameSolved < Constants.fisrtShowInterstitial) {
//            geziSizeY = Math.min(geziSizeY, 142 + ViewportUtils.getDeltaY() / 4);
//        } else {
//            if (gameData.gameIs == 0 && !gameData.isYindaoed[0]) {
//                geziSizeY = Math.min(128, geziSizeY);
//            } else {
//                geziSizeY = Math.min(geziSizeY, 134);
//            }
//        }
//
//
//        geziSizeX = radio * geziSizeY;
//        halfGeziSizeY = geziSizeY / 2;
//        halfGeziSizeX = geziSizeX / 2;
//
//        useImgSizeX = geziSizeX * 0.8f;
//        useImgSizeY = geziSizeY * 0.8f;
//
////		letterScaleV2 = (dx*7/8)/geziSizeX + 1;
////		System.out.println("letterScaleV2:" + letterScaleV2);
//
//
//        if (geziSizeX * 0.8f > 80) {
//            BitmapFont bitmapFont = Assets.getInstance().assetManager.get("ui/gameplayNew/font/arial_120.fnt", BitmapFont.class);
//            if (isSkinAlwaysBlack) {
//                bujuLabelStyle = new Label.LabelStyle(bitmapFont, Color.BLACK);
//                letterLableStyle = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());
//            } else {
//                bujuLabelStyle = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());
//                letterLableStyle = bujuLabelStyle;
//            }
//            bujuFontSize = 120;
//        } else {
//            BitmapFont bitmapFont = Assets.getInstance().assetManager.get("ui/gameplayNew/font/arial_80.fnt", BitmapFont.class);
//            if (isSkinAlwaysBlack) {
//                bujuLabelStyle = new Label.LabelStyle(bitmapFont, Color.BLACK);
//                letterLableStyle = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());
//            } else {
//                bujuLabelStyle = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());
//                letterLableStyle = bujuLabelStyle;
//            }
//            bujuFontSize = 80;
//        }
//
//        offsetX = new float[26];
//        offsetY = new float[26];
//        bujuFontScale = geziSizeX / bujuFontSize;
//        if (MyMathUtils.isEqual(80, bujuFontSize)) {
//            for (int i = 0; i < 26; i++) {
//                offsetX[i] = Constants.bujuLetterOffsetX80[i];
//                offsetY[i] = Constants.bujuLetterOffsetY80[i];
//            }
//        } else {
//            for (int i = 0; i < 26; i++) {
//                offsetX[i] = Constants.bujuLetterOffsetX120[i];
//                offsetY[i] = Constants.bujuLetterOffsetY120[i];
//            }
//        }
//
//
//        //notice:实际影响位置
//        float acx, acy;
//        acx = mx - (w * geziSizeX + (w - 1) * dx) / 2;
//        acy = my - (h * geziSizeY + (h - 1) * dy) / 2;
//
//
//        geziGroup = new Group[h][w];
//        letter = new Group[h][w];
//        letterLbl = new Label[h][w];
//        geziImages = new Image[h][w];
//        skinImages = new Image[h][w];
//        coinGeziImages = new Image[h][w];
//        fingerImages = new Image[h][w];
//        zmcxSpineGroup = new SpineGroup[h][w];
//        zmcxASpineGroup = new SpineGroup[h][w];
//        zmcxBSpineGroup = new SpineGroup[h][w];
//        gegSpineGroup = new SpineGroup[h][w];
//
//
//        geziPos = new Vector2[h][w];
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                geziPos[i][j] = new Vector2(acx + j * (dx + geziSizeX), acy + (h - 1 - i) * (dy + geziSizeY));
//            }
//        }
//
//        actualEy = acy;
//
//
//        boolean first = true;
//        boolean isCoin;
//        if(SpellingBeeManager.getInstance().getState() == SpellingBeeManager.State.GAMING && GameData.instance.gameMode == MyEnum.GameMode.normalMode && isNewLevel){
//            beePositions = new Position[maxLengthWord.length()];
//            bees = new SpineGroup[maxLengthWord.length()];
//            beeShow = GameData.instance.spellingBees;
//        }
//        //对每一个单词设置格子和字母
//        for (int i = 0; i < wordCount; i++) {
//            isCoin = false;
//            if (coinWord.equals(wordMsg[i].str)) {
//                isCoin = true;
//            }
//            int tx = wordMsg[i].x, ty = wordMsg[i].y;
//            //水平摆放
//            int wordLength = wordMsg[i].length;
//            if (wordMsg[i].dir == MyEnum.Dir.horizontal) {
//                for (int l = 0; l < wordLength; l++) {
//                    notPassSet.add(i);
//                    char useCh = wordMsg[i].str.charAt(l);
//                    int useY = ty + l;
//                    //先设置水平格子信息
//                    gridMsg[tx][useY].ch = useCh;
//                    gridMsg[tx][useY].appear = MyEnum.WordAppear.notAppear;
//                    gridMsg[tx][useY].hIndex = i;
//                    gridMsg[tx][useY].hLength = wordLength;
//                    gridMsg[tx][useY].hPos = l;
//                    gridMsg[tx][useY].has = true;
//
//                    //设置spelling bee
//                    if(beePositions!=null && wordMsg[i].str.equals(maxLengthWord)){
//                        initBee(l,tx,useY);
//                    }
//
//                    //当此处已经放入格子，即单词有重叠部分，跳过
//                    if (gridMsg[tx][useY].vIndex != -1) {
//                        continue;
//                    }
//
//
//                    //设置手指的块
//                    fingerImages[tx][useY] = new Image(fingerImagePatch);
////					fingerImages[tx][useY].setColor(kuaiColor[color % 4]);
//                    fingerImages[tx][useY].setSize(geziSizeX, geziSizeY);
//                    fingerImages[tx][useY].setVisible(false);
//                    stage.addActor(fingerImages[tx][useY]);
//                    fingerImages[tx][useY].setPosition(geziPos[tx][useY].x, geziPos[tx][useY].y);
//
//
//                    //设置格子
//                    if (geziGroup[tx][useY] == null) {
//                        geziGroup[tx][useY] = new Group();
//                        geziGroup[tx][useY].setTouchable(Touchable.disabled);
//                        geziGroup[tx][useY].setSize(geziSizeX, geziSizeY);
//                        geziGroup[tx][useY].setOrigin(geziSizeX / 2, geziSizeY / 2);
//                        geziGroup[tx][useY].setName("geziGroup");
//                    }
//                    geziGroup[tx][useY].setVisible(true);
//                    geziImages[tx][useY] = new Image(baikuaiPatch);
//                    skinImages[tx][useY] = new Image(skin);
//                    skinImages[tx][useY].setVisible(false);
//                    skinImages[tx][useY].setSize(geziSizeX, geziSizeY);
//
//
//                    geziImages[tx][useY].setSize(geziSizeX, geziSizeY);
//                    geziGroup[tx][useY].addActor(geziImages[tx][useY]);
//                    geziGroup[tx][useY].addActor(skinImages[tx][useY]);
//
//                    zmcxASpineGroup[tx][useY] = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineZmcxPath, SkeletonData.class));
//                    zmcxBSpineGroup[tx][useY] = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineZmcx2Path, SkeletonData.class));
//                    if (useKuaiB) {
//                        zmcxSpineGroup[tx][useY] = zmcxBSpineGroup[tx][useY];
//                        zmcxBSpineGroup[tx][useY].setVisible(true);
//                    } else {
//                        zmcxSpineGroup[tx][useY] = zmcxASpineGroup[tx][useY];
//                        zmcxASpineGroup[tx][useY].setVisible(true);
//                    }
//                    zmcxASpineGroup[tx][useY].setScale(geziSizeX / 130, geziSizeY / 130);
//                    zmcxASpineGroup[tx][useY].setPosition(geziSizeX / 2, geziSizeY / 2);
//
//                    zmcxBSpineGroup[tx][useY].setScale(geziSizeX / 130, geziSizeY / 130);
//                    zmcxBSpineGroup[tx][useY].setPosition(geziSizeX / 2, geziSizeY / 2);
//                    geziGroup[tx][useY].addActor(zmcxASpineGroup[tx][useY]);
//                    geziGroup[tx][useY].addActor(zmcxBSpineGroup[tx][useY]);
//
//                    zmcxASpineGroup[tx][useY].setVisible(false);
//                    zmcxBSpineGroup[tx][useY].setVisible(false);
//
//                    geziGroup[tx][useY].setPosition(geziPos[tx][useY].x, geziPos[tx][useY].y);
//                    stage.addActor(geziGroup[tx][useY]);
//                    bujuList.add(geziGroup[tx][useY]);
//
//                    if (first) {
//                        first = false;
//                    }
//
//
//                    //设置金币的块
//                    if (isCoin && gameData.gameIs == gameData.gameSolved) {
//                        coinGeziImages[tx][useY] = new Image(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/addcoin"));
//                        coinGeziImages[tx][useY].setSize(useImgSizeX, useImgSizeY);
//                        coinGeziImages[tx][useY].setPosition(geziGroup[tx][useY].getX() + halfGeziSizeX - useImgSizeX / 2, geziGroup[tx][useY].getY() + halfGeziSizeY - useImgSizeY / 2);
//                        gegSpineGroup[tx][useY] = new SpineGroup(Constants.spineGegPath);
//                        gegSpineGroup[tx][useY].setScale(geziSizeX / 130, geziSizeY / 130);
//                        gegSpineGroup[tx][useY].setPosition(geziSizeX / 2, geziSizeY / 2);
//                        gegSpineGroup[tx][useY].setAnimation("animation");
//                        geziGroup[tx][useY].addActor(gegSpineGroup[tx][useY]);
//                    }
//
//
//                    //设置字母
//                    letter[tx][useY] = new Group();
//                    letter[tx][useY].setName("letter");
//                    letter[tx][useY].setSize(geziSizeX, geziSizeY);
//                    letter[tx][useY].setVisible(false);
//
//                    char zimu = useCh;
//                    letterLbl[tx][useY] = new Label(zimu + "", letterLableStyle);
//                    if (useKuaiB) {
//                        letterLbl[tx][useY].setColor(Color.BLACK);
//                    }
//                    letterLbl[tx][useY].setAlignment(Align.bottomLeft);
//
//                    letterLbl[tx][useY].setFontScale(bujuFontScale * 0.8f);
//                    letterLbl[tx][useY].setPosition(halfGeziSizeX - letterLbl[tx][useY].getPrefWidth() / 2 + offsetX[zimu - 'A'] * bujuFontScale, halfGeziSizeY - letterLbl[tx][useY].getPrefHeight() / 2 + offsetY[zimu - 'A'] * bujuFontScale);
//
//
//                    letterLbl[tx][useY].setTouchable(Touchable.disabled);
//                    letter[tx][useY].addActor(letterLbl[tx][useY]);
//
//
//                    letter[tx][useY].setPosition(geziGroup[tx][useY].getX(), geziGroup[tx][useY].getY());
//                    letter[tx][useY].setOrigin(Align.center);
//                    stage.addActor(letter[tx][useY]);
//                    bujuList.add(letter[tx][useY]);
//
////					geziImages[tx][useY].setVisible(false);
//                }
//            } else if (wordMsg[i].dir == MyEnum.Dir.vertical) {
//                for (int l = 0; l < wordLength; l++) {
//                    notPassSet.add(i);
//                    //设置格子信息
//                    char useCh = wordMsg[i].str.charAt(l);
//                    int useX = tx + l;
//                    gridMsg[useX][ty].ch = useCh;
//                    gridMsg[useX][ty].appear = MyEnum.WordAppear.notAppear;
//                    gridMsg[useX][ty].vIndex = i;
//                    gridMsg[useX][ty].vLength = wordLength;
//                    gridMsg[useX][ty].vPos = l;
//                    gridMsg[useX][ty].has = true;
//
//                    //设置spelling bee
//                    if(wordMsg[i].str.equals(maxLengthWord)){
//                        initBee(l,useX,ty);
//                    }
//
//
//                    if (gridMsg[useX][ty].hIndex != -1)
//                        continue;
//
//
//                    //设置手指的块
//                    fingerImages[useX][ty] = new Image(fingerImagePatch);
////					fingerImages[useX][ty].setColor(kuaiColor[color % 4]);
//                    fingerImages[useX][ty].setSize(geziSizeX, geziSizeY);
//                    fingerImages[useX][ty].setVisible(false);
//                    stage.addActor(fingerImages[useX][ty]);
//                    fingerImages[useX][ty].setPosition(geziPos[useX][ty].x, geziPos[useX][ty].y);
//
//
//                    //设置格子
//                    if (geziGroup[useX][ty] == null) {
//                        geziGroup[useX][ty] = new Group();
//                        geziGroup[useX][ty].setTouchable(Touchable.disabled);
//                        geziGroup[useX][ty].setSize(geziSizeX, geziSizeY);
//                        geziGroup[useX][ty].setOrigin(geziSizeX / 2, geziSizeY / 2);
//                        geziGroup[useX][ty].setName("geziGroup");
//                    }
//                    geziGroup[useX][ty].setVisible(true);
//                    geziImages[useX][ty] = new Image(baikuaiPatch);
//                    skinImages[useX][ty] = new Image(skin);
//                    skinImages[useX][ty].setVisible(false);
//                    skinImages[useX][ty].setSize(geziSizeX, geziSizeY);
//
////					geziImages[useX][ty].setColor(kuaiColor[color % 4]);
////					color ++;
//
//                    geziImages[useX][ty].setSize(geziSizeX, geziSizeY);
//                    geziGroup[useX][ty].addActor(geziImages[useX][ty]);
//                    geziGroup[useX][ty].addActor(skinImages[useX][ty]);
//
//
//                    zmcxASpineGroup[useX][ty] = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineZmcxPath, SkeletonData.class));
//                    zmcxBSpineGroup[useX][ty] = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineZmcx2Path, SkeletonData.class));
//                    if (useKuaiB) {
//                        zmcxSpineGroup[useX][ty] = zmcxBSpineGroup[useX][ty];
//                        zmcxBSpineGroup[useX][ty].setVisible(true);
//                    } else {
//                        zmcxSpineGroup[useX][ty] = zmcxASpineGroup[useX][ty];
//                        zmcxASpineGroup[useX][ty].setVisible(true);
//                    }
//                    zmcxASpineGroup[useX][ty].setScale(geziSizeX / 130, geziSizeY / 130);
//                    zmcxASpineGroup[useX][ty].setPosition(geziSizeX / 2, geziSizeY / 2);
//
//                    zmcxBSpineGroup[useX][ty].setScale(geziSizeX / 130, geziSizeY / 130);
//                    zmcxBSpineGroup[useX][ty].setPosition(geziSizeX / 2, geziSizeY / 2);
//                    geziGroup[useX][ty].addActor(zmcxASpineGroup[useX][ty]);
//                    geziGroup[useX][ty].addActor(zmcxBSpineGroup[useX][ty]);
//
//                    zmcxASpineGroup[useX][ty].setVisible(false);
//                    zmcxBSpineGroup[useX][ty].setVisible(false);
//
//
//                    geziGroup[useX][ty].setPosition(geziPos[useX][ty].x, geziPos[useX][ty].y);
//                    stage.addActor(geziGroup[useX][ty]);
//                    bujuList.add(geziGroup[useX][ty]);
//                    if (first) {
//                        first = false;
//                    }
//
//
//                    //设置金币的块
//                    if (isCoin && gameData.gameIs == gameData.gameSolved) {
//                        coinGeziImages[useX][ty] = new Image(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/addcoin"));
//                        coinGeziImages[useX][ty].setSize(useImgSizeX, useImgSizeY);
//                        coinGeziImages[useX][ty].setPosition(geziGroup[useX][ty].getX() + halfGeziSizeX - useImgSizeX / 2, geziGroup[useX][ty].getY() + halfGeziSizeY - useImgSizeY / 2);
//
//                        gegSpineGroup[useX][ty] = new SpineGroup(Constants.spineGegPath);
//                        gegSpineGroup[useX][ty].setScale(geziSizeX / 130, geziSizeY / 130);
//                        gegSpineGroup[useX][ty].setPosition(geziSizeX / 2, geziSizeY / 2);
//                        gegSpineGroup[useX][ty].setAnimation("animation");
//                        geziGroup[useX][ty].addActor(gegSpineGroup[useX][ty]);
////						geziImages[useX][ty].setColor(kuaiColor[4]);
////						fingerImages[useX][ty].setColor(kuaiColor[4]);
////						stage.addActor(coinGeziImages[useX][ty]);
//                    }
//
//
//                    //设置字母
//                    letter[useX][ty] = new Group();
//                    letter[useX][ty].setName("letter");
//                    letter[useX][ty].setSize(geziSizeX, geziSizeY);
//                    letter[useX][ty].setVisible(false);
//
//                    char zimu = useCh;
//                    letterLbl[useX][ty] = new Label(zimu + "", letterLableStyle);
//
//                    if (useKuaiB) {
//                        letterLbl[useX][ty].setColor(Color.BLACK);
//                    }
//
//
//                    letterLbl[useX][ty].setAlignment(Align.bottomLeft);
//                    letterLbl[useX][ty].setFontScale(bujuFontScale * 0.8f);
//                    letterLbl[useX][ty].setPosition(halfGeziSizeX - letterLbl[useX][ty].getPrefWidth() / 2 + offsetX[zimu - 'A'] * bujuFontScale, halfGeziSizeY - letterLbl[useX][ty].getPrefHeight() / 2 + offsetY[zimu - 'A'] * bujuFontScale);
//
//                    letterLbl[useX][ty].setTouchable(Touchable.disabled);
//                    letter[useX][ty].addActor(letterLbl[useX][ty]);
//
//                    letter[useX][ty].setPosition(geziGroup[useX][ty].getX(), geziGroup[useX][ty].getY());
//
//                    letter[useX][ty].setOrigin(Align.center);
//                    stage.addActor(letter[useX][ty]);
//                    bujuList.add(letter[useX][ty]);
//
////					geziImages[useX][ty].setVisible(false);
//                }
//            }
//        }
//
//        setBeePositionIndex();
//
//        if (isNewLevel) {
//            //之前的hint不认为是hint
//            //放置之前的hint
//            for (int i = 0; i < h; i++) {
//                for (int j = 0; j < w; j++) {
//                    if (gameData.hintAppear[i][j] && gridMsg[i][j] != null) {
//                        int vid = gridMsg[i][j].vIndex, hid = gridMsg[i][j].hIndex;
//                        if (!isFind(vid) && !isFind(hid)) {
//                            gridMsg[i][j].appear = MyEnum.WordAppear.hint;
//                            initFindLetter(i,j);
//                        }
//                    }
//
//                }
//            }
//
//            for (int i = 0; i < wordCount; i++) {
//                if (isFind(i)) {
//                    gameData.findWord[gameData.findWordLength] = gameAnswer[i];
//                    gameData.findWordLength++;
//                }
//            }
//
//            //放置之前能连成串的单词
//            for (int i = 0; i < wordCount; i++) {
//                if (isFind(i)) {
//                    addComboTimes = 0;
//                    notPassSet.remove(i);
//                    int tx = wordMsg[i].x;
//                    int ty = wordMsg[i].y;
//                    int kx, ky;
//                    if (wordMsg[i].dir == MyEnum.Dir.horizontal) {
//                        kx = 0;
//                        ky = 1;
//                    } else {
//                        kx = 1;
//                        ky = 0;
//                    }
//                    for (int j = 0; j < gameAnswer[i].length(); j++) {
//                        int xx = tx + kx * j;
//                        int yy = ty + ky * j;
//                        gridMsg[xx][yy].appear = MyEnum.WordAppear.appear;
//                        initFindLetter(xx,yy);
//
//                    }
//
//                }
//            }
//        }
//
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                if (gridMsg[i][j] != null) {
//                    int vIndex = gridMsg[i][j].vIndex;
//                    int hIndex = gridMsg[i][j].hIndex;
//                    if ((vIndex != -1 || hIndex != -1)&& (gridMsg[i][j].appear== MyEnum.WordAppear.empty || gridMsg[i][j].appear == MyEnum.WordAppear.notAppear)) {
//                        tPosition.set(i, j, hIndex, vIndex);
//                        emptyBox.add(tPosition.deepClone());
//                    }
//                }
//            }
//        }
//
//
//        try {
//            emptyBox.init();
//        }catch (Exception e){
//            if(!gameData.errorTry){
//                clearCurrentData();
//                gotoScreen("GameplayScreen");
//                gameData.errorTry = true;
//                return;
//            }
//            e.printStackTrace();
//        }
//
//
////		stage.addActor(bujuGroup);
//
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                if (coinGeziImages[i][j] != null) {
//                    stage.addActor(coinGeziImages[i][j]);
//                    bujuList.add(coinGeziImages[i][j]);
//                }
//            }
//        }
//
//
//        if (gameMode != MyEnum.GameMode.normalMode) {
//            dailyProgress.initProgress(dailyJindu);
//            feng = new Gemstone();
//            gemScale = geziSizeX/130F;
//            feng.setScale(gemScale);
////            feng.setSize(geziSizeX * 3 / 4, geziSizeX * 3 / 4);
//            stage.addActor(feng);
//            bujuList.add(feng);
//            if (fengPosition.index != -1) {
//                //之前已经有枫叶玩过了
//                try {
//                    resetGemstone(geziGroup[fengPosition.x][fengPosition.y].getX() + halfGeziSizeX - feng.getWidth() / 2, geziGroup[fengPosition.x][fengPosition.y].getY() + halfGeziSizeY - feng.getHeight() / 2);
//                }catch (Exception e){
//                    fengPosition = new Position();
//                    getEmptyGrid(-1);
//                }
//            } else {
//                //第一次进入这一天
//                getEmptyGrid(-1);
//            }
//        }
//
//
//        //添加格子的点击事件
//        setGeziListener();
//
//
//        if (Constants.DrawGezi) {
//            drawGezi();
//        }
//    }
//
//    private void initFindLetter(int xx,int yy){
//        geziGroup[xx][yy].setVisible(true);
//        letter[xx][yy].setVisible(true);
//        if(gridMsg[xx][yy].appear == MyEnum.WordAppear.appear){
//            skinImages[xx][yy].setVisible(true);
//            geziImages[xx][yy].setVisible(false);
//            if (isSkinAlwaysBlack) {
//                letterLbl[xx][yy].setColor(Color.BLACK);
//            } else {
//                letterLbl[xx][yy].setColor(Color.WHITE);
//            }
//        }else {
//            skinImages[xx][yy].setVisible(false);
//            geziImages[xx][yy].setVisible(true);
//        }
//
//        if(coinGeziImages[xx][yy]!=null) {
//            coinGeziImages[xx][yy].setVisible(false);
//            gegSpineGroup[xx][yy].setVisible(false);
//        }
//
//        int beeIndex = getBeeIndex(xx,yy);
//        if(beeIndex !=-1){
//            if(isBeeEnable(beeIndex)){
//                beeState |= (1 << beeIndex);
//            }
//        }
//    }
//
//
//    //让字母全显示出来，看看样子的
//    void drawGezi() {
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                if (letter[i][j] != null) {
//                    letter[i][j].setVisible(true);
//                    skinImages[i][j].setVisible(true);
//                    if (coinGeziImages[i][j] != null) {
//
//                        coinGeziImages[i][j].setVisible(true);
//                        letter[i][j].toFront();
//                    }
//                }
//
//            }
//        }
//
//    }
//
//
//    //给格子添加点击事件，显示词典面板
//    void setGeziListener() {
//
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//
//                if (letter[i][j] != null) {
//                    final int x1 = i;
//                    final int y1 = j;
//                    letterLbl[i][j].setTouchable(Touchable.disabled);
//                    letter[i][j].addListener(new ButtonClickListener(false, Constants.BTN_TYPE_CLICK) {
//                        @Override
//                        public void clicked(InputEvent event, float x, float y) {
//                            super.clicked(event, x, y);
//
//                            //当格子被发现的时候
//                            if (gridMsg[x1][y1].appear == MyEnum.WordAppear.appear) {
//                                //垂直单词和竖直单词背找到的标志
//                                boolean isVFind = false, isHFind = false;
//                                if (gridMsg[x1][y1].vIndex != -1) {
//                                    isVFind = isFind(gridMsg[x1][y1].vIndex);
//                                }
//                                if (gridMsg[x1][y1].hIndex != -1) {
//                                    isHFind = isFind(gridMsg[x1][y1].hIndex);
//                                }
//
//                                if (isVFind && isHFind) {
//                                    int wordIndex1 = 0, wordIndex2 = 0;
//                                    for (int i = 0; i < gameData.findWordLength; i++) {
//                                        if (gameData.findWord[i].equals(wordMsg[gridMsg[x1][y1].vIndex].str)) {
//                                            wordIndex1 = i;
//                                        }
//                                        if (gameData.findWord[i].equals(wordMsg[gridMsg[x1][y1].hIndex].str)) {
//                                            wordIndex2 = i;
//                                        }
//                                    }
//                                    clickWordPanel.setWord(wordMsg[gridMsg[x1][y1].vIndex].str, wordIndex1, wordMsg[gridMsg[x1][y1].hIndex].str, wordIndex2);
//                                    showPanel(clickWordPanel);
//                                    PlatformManager.instance.outPut("WordClick", "word", "" + useToday);
//                                } else if (isVFind) {
//                                    int wordIndex1 = 0;
//                                    for (int i = 0; i < gameData.findWordLength; i++) {
//                                        if (gameData.findWord[i].equals(wordMsg[gridMsg[x1][y1].vIndex].str)) {
//                                            wordIndex1 = i;
//                                            break;
//                                        }
//                                    }
//                                    clickWordPanel.setWord(wordMsg[gridMsg[x1][y1].vIndex].str, wordIndex1, null, 0);
//                                    showPanel(clickWordPanel);
//                                    PlatformManager.instance.outPut("WordClick", "word", "" + useToday);
//                                } else if (isHFind) {
//                                    int wordIndex1 = 0;
//                                    for (int i = 0; i < gameData.findWordLength; i++) {
//                                        if (gameData.findWord[i].equals(wordMsg[gridMsg[x1][y1].hIndex].str)) {
//                                            wordIndex1 = i;
//                                            break;
//                                        }
//                                    }
//                                    clickWordPanel.setWord(wordMsg[gridMsg[x1][y1].hIndex].str, wordIndex1, null, 0);
//                                    showPanel(clickWordPanel);
//                                    PlatformManager.instance.outPut("WordClick", "word", "" + useToday);
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        }
//    }
//
//
//    MyLine myLine;
//
//    void initMyLine() {
//        myLine = new MyLine(letterLength, skinColor);
//        pinGroup.addActor(myLine);
//        myLine.setZIndex(lettersGroup.getZIndex());
////		stage.addActor(myLine);
//    }
//
//    //初始化引导虚线
//    YindaoXuxian yindaoXuxian[];
//
//    void initYindaoXuxian() {
//        //7个字母，最多6根线
//        yindaoXuxian = new YindaoXuxian[6];
//        for (int i = 0; i < 6; i++) {
//            yindaoXuxian[i] = new YindaoXuxian();
//            stage.addActor(yindaoXuxian[i]);
//            yindaoXuxian[i].setVisible(false);
//        }
//    }
//
//
//    DailyProgress dailyProgress;
//    boolean useWhiterPin;
//    Image pin;
//
//    Group pinGroup;
//    Vector2 pinGroupPos;
//    Vector2 dailyProgressPos;
//
//
//    void initDi() {
//        coinGroupLoad();
//        addCoinsGroupLoad();
//
//
//        pinGroup = new Group();
//        //这里要把pin单独拿出来
//        pin = baseGroup.findActor("pin");
//
//
//        pinGroup.addActor(pin);
//
//        if (!useKuaiB) {
//            //引导时候用黑盘
//            pin.setColor(0, 0, 0, 0.5f);
//            useWhiterPin = false;
//        } else {
//            pin.setColor(1, 1, 1, 0.7f);
//            useWhiterPin = true;
//        }
//
//
////		pin.setVisible(false);
//
//        if (gameData.gameSolved < Constants.fisrtShowInterstitial) {
//            pinGroup.setPosition(pin.getX(), 30 - ViewportUtils.getDeltaY());
//        } else {
//            pinGroup.setPosition(pin.getX(), 97 - ViewportUtils.getDeltaY() / 2);
//        }
//
//
//        pinGroupPos = new Vector2(pinGroup.getX(), pinGroup.getY());
//
//        pinGroup.setSize(pin.getWidth(), pin.getHeight());
//        pinGroup.setOrigin(Align.center);
//        pin.setPosition(0, 0);
//
////		baseGroup.findActor("guanggaolan").setY(ViewportUtils.getBottom());
//
//        stage.addActor(pinGroup);
//
////		pin.setColor(Color.WHITE);
//
//
//        if (gameMode == MyEnum.GameMode.normalMode) {
//            baseGroup.findActor("dailyProgress").setVisible(false);
//        } else {
//            dailyProgress = new DailyProgress(baseGroup.findActor("dailyProgress"), wordCount, new DailyProgress.DailyListener() {
//                @Override
//                public void achieve(int id) {
//                    int coinNum = 0;
//                    switch (id){
//                        case 0:
//                            coinNum = 15;
//                            break;
//                        case 1:
//                            coinNum = 25;
//                            break;
//                        case 2:
//                            coinNum = 35;
//                            break;
//                        case 3:
//                            coinNum = 50;
//                            break;
//                    }
//                    RuntimeData.instance.dailyRewardCoin = coinNum;
//
//                }
//            });
//            dailyProgress.setY(dailyProgress.getY() + ViewportUtils.getDeltaY());
//            stage.addActor(dailyProgress);
//            dailyProgressPos = new Vector2(dailyProgress.getX(), dailyProgress.getY());
//        }
//    }
//
//    @Override
//    void coinGroupLoad() {
//        super.coinGroupLoad();
//        if (gameData.gameSolved == 0) {
//            coinGroup.setCanVisible(false);
//            coinGroup.setVisible(false);
//        }else{
//            coinGroup.setCanVisible(true);
//        }
//    }
//
//    SpineGroup guliciSpineGroup;
//    Group comboGroup;
//    //初始化连击的动画和粒子
//    SpineGroup comboSpineGroup;
//
//    void initCombo() {
//        comboGroup = new Group();
//        comboGroup.setVisible(false);
//        comboGroup.setTouchable(Touchable.disabled);
//        pinGroup.addActor(comboGroup);
//        pinGroup.setZIndex(baseGroup.getZIndex() + 1);
//        if(GameData.instance.isGoodPhone) {
//            comboSpineGroup = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineComboPath, SkeletonData.class));
//            comboGroup.setSize(comboSpineGroup.getWidth(), comboSpineGroup.getHeight());
//            comboGroup.setOrigin(Align.center);
//            comboGroup.addActor(comboSpineGroup);
//            comboSpineGroup.setAnimation("animation2");
//        }
//        comboGroup.setPosition(pinGroup.getWidth() / 2, pinGroup.getHeight() / 2);
//
//
//        guliciSpineGroup = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineGuliciPath, SkeletonData.class));
//        guliciSpineGroup.setTouchable(Touchable.disabled);
//        guliciSpineGroup.setOrigin(Align.center);
//        guliciSpineGroup.setPosition(360, 573);
//        guliciSpineGroup.setScale(1.2f);
//        stage.addActor(guliciSpineGroup);
//        guliciSpineGroup.setVisible(false);
//    }
//
//    String levelText;
//    LevelButton levelBtn;
//    Vector2 levelBtnPos;
//
//    void levelButtonLoad() {
//
//        levelBtn = new LevelButton(baseGroup.findActor("levelBtn"), "", 1);
//
//
//        stage.addActor(levelBtn);
////		levelBtn.setFontScale(36f/45f);
//        levelBtn.setPosition(levelBtn.getX() + ViewportUtils.getLeft(), levelBtn.getY() + ViewportUtils.getDeltaY());
////		levelBtn.setPosition(360- levelBtn.getPrefWidth()/2,1204+ViewportUtils.getDeltaY());
//        levelBtnPos = new Vector2(levelBtn.getX(), levelBtn.getY());
//
//        levelBtn.addListener(new ButtonClickListener(false, Constants.BTN_TYPE_CLICK) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                if (gameMode == MyEnum.GameMode.normalMode) {
//                    gameplayGotoScreen(()->{
//                        gameData.startScreenAlpha = true;
//                        PlatformManager.instance.gotoScreen("StartScreen");
//                    });
//                } else {
//                    PlatformManager.instance.showPanel("DailyPanel");
//                }
//            }
//        });
//    }
//
//
//    public TextureRegionDrawable fingerImagePatch;
//    public TextureRegionDrawable baikuaiPatch;
//    public TextureRegionDrawable skin;
//
//
//    Color skinColor;
//
//    void initThemeRegions() {
//
//        fingerImagePatch = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/atlas/fingerkuai")));
//        if (useKuaiB) {
//            baikuaiPatch = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/atlas/kuaib")));
//
//        } else {
//            baikuaiPatch = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/atlas/kuai")));
//        }
//
////		baikuaiy = new TextureRegionDrawable(GameplayAssets.baikuaiy);
//
//        int index = Integer.parseInt(skinName.replace("skin", ""));
//        skin = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/skin/skin" + index)));
////		TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
////		textureParameter.genMipMaps = false;
////		textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
////		textureParameter.magFilter = Texture.TextureFilter.Linear;
////		textureParameter.wrapU = Texture.TextureWrap.ClampToEdge;
////		textureParameter.wrapV = Texture.TextureWrap.ClampToEdge;
////		Assets.getInstance().assetManager.load("test/skin4.png", Texture.class, new TextureLoader.TextureParameter());
////		Assets.getInstance().assetManager.finishLoading();
////
////		skin = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("test/skin4.png", Texture.class)));
//
//
//        skinColor = Constants.SkinColor[index];
//
//
//    }
//
//    @Override
//    void PanelLoad() {
//        super.PanelLoad();
//        YindaoPanelLoad();
//        ClickWordPanelLoad();
//        AllWordPanelLoad();
//        ExtraWordPanelLoad();
//        ExtraWordPanel2Load();
//        PropRewardPanelLoad();
//
//
//        StrikePanelLoad();
//        if (gameMode == MyEnum.GameMode.normalMode) {
//            LevelPassPanelLoadC();
//            NovicesRewardPanelLoad();
//            RatePanelLoad();
//        }
//        if(gameData.gameIs == 10 && gameData.coinNumber< 60 ){
//            CoinSupplyPanel coinSupplyPanel = new CoinSupplyPanel(game);
//            addPanel(coinSupplyPanel);
//        }
//        if(gameMode == MyEnum.GameMode.normalMode && SpellingBeeManager.getInstance().getState() != SpellingBeeManager.State.NONE){
//            SpellingBeePanelLoad();
//        }
//        DailyGuidePanelLoad();
//    }
//
//
//    StrikePanel strikePanel;
//    void StrikePanelLoad(){
//        strikePanel = new StrikePanel(game);
//        addPanel(strikePanel);
//    }
//
//
//    DailyGuidePanel dailyGuidePanel;
//    void DailyGuidePanelLoad(){
//        dailyGuidePanel = new DailyGuidePanel(game);
//        addPanel(dailyGuidePanel);
//    }
//
//
//    YindaoPanel yindaoPanel;
//
//    void YindaoPanelLoad() {
//        yindaoPanel = new YindaoPanel(game);
//        addPanel(yindaoPanel);
//    }
//
//    ClickWordPanel clickWordPanel;
//
//    void ClickWordPanelLoad() {
//        clickWordPanel = new ClickWordPanel(game);
//        addPanel(clickWordPanel);
//    }
//
//    AllWordPanel allWordPanel;
//
//    void AllWordPanelLoad() {
//        allWordPanel = new AllWordPanel(game);
//        addPanel(allWordPanel);
//
//        //将额外单词添加入词典
//        for (int i = 0; i < extraWord.length; i++) {
//            if (gameData.extraState.charAt(i) == '1') {
//                allWordPanel.addWord(extraWord[i], true);
//            }
//        }
//
//        for (int i = 0; i < wordCount; i++) {
//            if (isFind(i)) {
//                allWordPanel.addWord(wordMsg[i].str, false);
//            }
//        }
//
//    }
//
//
//    ExtraWordPanel extraWordPanel;
//
//    void ExtraWordPanelLoad() {
//        extraWordPanel = new ExtraWordPanel(game);
//        addPanel(extraWordPanel);
//
//        //将额外单词添加入词典
//        for (int i = 0; i < extraWord.length; i++) {
//            if (gameData.extraState.charAt(i) == '1') {
//                extraWordPanel.addWord(extraWord[i]);
//            }
//        }
//    }
//
//    ExtraWordPanel2 extraWordPanel2;
//
//    void ExtraWordPanel2Load() {
//        extraWordPanel2 = new ExtraWordPanel2(game);
//        addPanel(extraWordPanel2);
//    }
//
//    public LevelPassPanel levelPassPanel;
//
//    void LevelPassPanelLoadC() {
//        levelPassPanel = new LevelPassPanel(game);
//        addPanel(levelPassPanel);
//    }
//    public NovicesRewardPanel novicesRewardPanel;
//
//    void NovicesRewardPanelLoad() {
//        novicesRewardPanel = new NovicesRewardPanel(game);
//        addPanel(novicesRewardPanel);
//    }
//
//    public PropRewardPanel propRewardPanel;
//
//    void PropRewardPanelLoad() {
//        propRewardPanel = new PropRewardPanel(game);
//        addPanel(propRewardPanel);
//    }
//
//    SpellingBeePanel spellingBeePanel;
//
//    void SpellingBeePanelLoad(){
//        spellingBeePanel = new SpellingBeePanel(game);
//        addPanel(spellingBeePanel);
//    }
//
//    RatePanel ratePanel;
//
//    void RatePanelLoad() {
//        ratePanel = new RatePanel(game);
//        addPanel(ratePanel);
//    }
//
//
//    void ButtonLoad() {
//        extraWordButtonLoad();
//        fasthintButtonLoad();
//        hintButtonLoad();
//        fingerButtonLoad();
//        shuffleButtonLoad();
//        videoButtonLoad();
//
//        //词典按钮载入要放在额外单词载入的后面
//        dictBtnLoad();
//
//        levelButtonLoad();
//
//
//        ButtonSet();
//        if(gameMode == MyEnum.GameMode.normalMode && SpellingBeeManager.getInstance().getState() != SpellingBeeManager.State.NONE){
//            spellBeeButtonLoad();
//        }
//    }
//
//    //设置部分按钮可见性和可用性
//    void ButtonSet() {
//        //设置hint按钮
//        if (gameData.gameSolved < Constants.hintYindaoStart) {
//            hintBtn.setCanVisible(false);
//            hintBtn.setVisible(false);
//        }
//
//
//        //设置fasthint按钮
//        if (gameData.gameSolved < Constants.fasthintStart) {
//            fasthintBtn.setCanVisible(false);
//            fasthintBtn.setVisible(false);
//        }
//
//        if (gameData.gameIs == Constants.fasthintStart && !gameData.isYindaoed[15] && gameMode == MyEnum.GameMode.normalMode) {
//            fasthintBtn.setFree();
//        } else {
//            fasthintBtn.setNum(gameData.fasthintNum);
//        }
//        if (gameData.gameSolved >= Constants.fasthintStart) {
//            fasthintBtn.setDisable(false);
//
//        } else {
//            fasthintBtn.setDisable(true);
//        }
//
//
//        //设置finger按钮
//        if (gameData.gameSolved < Constants.fingerStart) {
//            fingerBtn.setCanVisible(false);
//            fingerBtn.setVisible(false);
//        }
//
//
//        if (gameData.gameIs == Constants.fingerStart && !gameData.isYindaoed[2] && gameMode == MyEnum.GameMode.normalMode || Prefs.getBoolean("isFingerFree", false)) {
//            if(GameData.instance.dnaSetOutput){
//                fingerBtn.setFreeB(gameData.fingerNum);
//            }else {
//                fingerBtn.setFree();
//            }
//        } else {
//            fingerBtn.setNum(gameData.fingerNum);
//        }
//
//
//        //设置shuffle按钮
//        if (gameData.gameSolved < Constants.hintYindaoStart) {
//            shuffleBtn.setCanVisible(false);
//            shuffleBtn.setVisible(false);
//        }
//
//        videoBtn.setCanVisible(true);
//
//        //设置watch按钮和词典按钮
//        if (gameData.gameSolved == 0) {
//            videoBtn.setVisible(false);
//            videoBtn.setCanVisible(false);
//            videoBtnClosed = true;
//            dicBtnCanVisible = false;
//            dictBtn.setVisible(false);
//        }
//
//
//        if (gameData.findWordLength == 0 && gameData.findExtraWordLength == 0) {
//            dictBtn.setVisible(false);
//        }
//
//        if (gameData.gameSolved < Constants.fisrtShowInterstitial) {
//            float deltaY = ViewportUtils.getDeltaY() / 2;
//            videoBtn.setY(videoBtn.getY()-67-deltaY);
//            videoBtnPos.y = videoBtn.getY();
//            hintBtn.setY(hintBtn.getY() - 67 - deltaY);
//            hintBtnPos.y = hintBtn.getY();
//
//            shuffleBtn.setY(shuffleBtn.getY() - 67 - deltaY);
//            shuffleBtnPos.y = shuffleBtn.getY();
//
//            fingerBtn.setY(fingerBtn.getY() - 67 - deltaY);
//            fingerBtnPos.y = fingerBtn.getY();
//            if(fingerX!=null){
//                fingerX.setY(fingerBtnPos.y);
//            }
//
//            fasthintBtn.setY(fasthintBtn.getY() - 67 - deltaY);
//            fasthintBtnPos.y = fasthintBtn.getY();
//
//            if(spellingBeeBtn!=null){
//                spellingBeeBtn.setY(spellingBeeBtn.getY()-67 - deltaY);
//                spellingBeeBtnPos.y = spellingBeeBtn.getY();
//            }
//        }
//    }
//
//
//    //	Group extraWordGroup;
//    //额外单词网上飞的label
////	Label extraWordLbl;
//    final int extraWordGroupMaxn = 5;
//    boolean extraWordJinbiSpineGroupUse[];
//    SpineGroup extraWordJinbiSpineGroup[];
//    Preview extraWordGroup[];
//    boolean extraWordGroupUse[];
//
//    void extraWordButtonLoad() {
//
//        extraWordGroup = new Preview[extraWordGroupMaxn];
//        extraWordGroupUse = new boolean[extraWordGroupMaxn];
//        for (int i = 0; i < extraWordGroupMaxn; i++) {
//            extraWordGroup[i] = new Preview(skinColor, bujuLabelStyle, bujuFontSize);
//            stage.addActor(extraWordGroup[i]);
//            extraWordGroup[i].setVisible(false);
//            extraWordGroupUse[i] = false;
//        }
//
//
//        extraWordJinbiSpineGroup = new SpineGroup[extraWordGroupMaxn];
//        extraWordJinbiSpineGroupUse = new boolean[extraWordGroupMaxn];
//        for (int i = 0; i < extraWordGroupMaxn; i++) {
//            final int t = i;
//            extraWordJinbiSpineGroup[i] = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineJinbiPath),
//                    new AnimationState.AnimationStateListener() {
//                        @Override
//                        public void start(AnimationState.TrackEntry trackEntry) {
//
//                        }
//
//                        @Override
//                        public void interrupt(AnimationState.TrackEntry trackEntry) {
//
//                        }
//
//                        @Override
//                        public void end(AnimationState.TrackEntry trackEntry) {
//
//                        }
//
//                        @Override
//                        public void dispose(AnimationState.TrackEntry trackEntry) {
//
//                        }
//
//                        @Override
//                        public void complete(AnimationState.TrackEntry trackEntry) {
//                            if (extraWordJinbiSpineGroup[t].getAnimationName().equals("tan")) {
//                                extraWordJinbiSpineGroupUse[t] = false;
//                                extraWordJinbiSpineGroup[t].setVisible(false);
//                            }
//                        }
//
//                        @Override
//                        public void event(AnimationState.TrackEntry trackEntry, Event event) {
//
//                        }
//                    });
//            stage.addActor(extraWordJinbiSpineGroup[i]);
//
//            extraWordJinbiSpineGroupUse[i] = false;
//            extraWordJinbiSpineGroup[i].setVisible(false);
//        }
//
//
//    }
//
//
//    GamePlayButton fasthintBtn;
//    Vector2 fasthintBtnPos;
//
//    void fasthintButtonLoad() {
//        Group t = gameplayButtonUIEditor.createGroup();
//        int costValue;
//        costValue = Constants.fasthintCost;
//
//        fasthintBtn = new GamePlayButton(t, new TextureRegionDrawable(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/fasthint")), 1, MyEnum.PropType.fasthint, costValue);
//        fasthintBtn.setPosition(ViewportUtils.getLeft()+7, 277 - ViewportUtils.getDeltaY() / 2);
//        stage.addActor(fasthintBtn);
//        fasthintBtnPos = new Vector2(fasthintBtn.getX(), fasthintBtn.getY());
//        fastHintSpineGroup = new SpineGroup[6];
//        for(int i = 0;i<fastHintSpineGroup.length;i++){
//            final int id = i;
//            fastHintSpineGroup[i] = new SpineGroup(Assets.getInstance().assetManager.get(Constants.spineFastHintcalmPath), new AnimationState.AnimationStateAdapter() {
//                @Override
//                public void complete(AnimationState.TrackEntry entry) {
//                    fastHintSpineGroup[id].remove();
//                }
//            });
//        }
//
//
//        fastHintSpineGroup2 = new SpineGroup[6];
//        for(int i = 0;i<fastHintSpineGroup2.length;i++){
//            final int id = i;
//            fastHintSpineGroup2[i] = new SpineGroup(Assets.getInstance().assetManager.get(Constants.spineFastHintfankuiPath), new AnimationState.AnimationStateAdapter() {
//                @Override
//                public void complete(AnimationState.TrackEntry entry) {
//                    fastHintSpineGroup2[id].remove();
//                }
//            });
//            fastHintSpineGroup2[i].setScale(geziSizeX/82F);
//        }
//
//
//        fastHintCloudSpineGroup = new SpineGroup(Assets.getInstance().assetManager.get(Constants.spineFastHintYunPath), new AnimationState.AnimationStateAdapter() {
//            @Override
//            public void complete(AnimationState.TrackEntry entry) {
//                fastHintCloudSpineGroup.remove();
//                mask.addAction(Actions.sequence(Actions.alpha(0,0.15F),Actions.visible(false)));
//            }
//        });
//        fastHintCloudSpineGroup.setVisible(false);
//
//        fasthintBtn.addListener(new ButtonClickListener(false, Constants.BTN_TYPE_USEITEM) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//                if (!fasthintBtn.getDisable()) {
//                    playBtnSound();
//                    ddnaItemUseState = fasthint();
//                } else {
//                    playBtnSound();
//                    int leftLevel;
//                    leftLevel = Constants.fasthintStart - gameData.gameSolved;
//                    leftLevel = Math.max(1, leftLevel);
//                    if (leftLevel == 1) {
//                        showWarningGroup("New props will be unlocked after " + leftLevel + " level!");
//                    } else {
//                        showWarningGroup("New props will be unlocked after " + leftLevel + " levels!");
//                    }
//                }
//
//
//                if (gameMode == MyEnum.GameMode.normalMode) {
//                    if (ddnaItemUseState == MyEnum.DDNAItemUseState.useFree || ddnaItemUseState == MyEnum.DDNAItemUseState.useExistingItem) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item3", 0, gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                    } else if (ddnaItemUseState == MyEnum.DDNAItemUseState.useCoin) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item3", costValue, gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                    }
//                } else {
//
//                    if (ddnaItemUseState == MyEnum.DDNAItemUseState.useFree || ddnaItemUseState == MyEnum.DDNAItemUseState.useExistingItem) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item3", 0, gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                    } else if (ddnaItemUseState == MyEnum.DDNAItemUseState.useCoin) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item3", costValue, gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                    }
//                }
//                addComboTimes = 0;
//
//
//            }
//        });
//
//
//    }
//
//    private boolean fastHintShow = false;
//
//    public MyEnum.DDNAItemUseState fasthint() {
//        MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//        if (emptyBox.size() > 0) {
//            fastHintShow = true;
//            setInputProcessor(false);
//            pinGroup.toFront();
//            mask.setZIndex(pinGroup.getZIndex()+1);
//            pinGroup.toFront();
//            if (fasthintBtn.isFree()) {
//                gameData.setYindao(15);
//                PlatformManager.instance.hidePanel(yindaoPanel);
//                gameData.updateFastHintNum(1,true);
//                fasthintBtn.setNum(gameData.fasthintNum);
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useFree;
//            } else if (gameData.fasthintNum > 0) {
//                gameData.updateFastHintNum(-1,true);
//                fasthintBtn.setNum(gameData.fasthintNum);
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useExistingItem;
//            } else if (gameData.coinNumber >= fasthintBtn.getCoinValue()) {
//                gameData.subCoinNumber(fasthintBtn.getCoinValue());
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useCoin;
//            } else {
//                gameData.buyLessCoin = fasthintBtn.getCoinValue() - gameData.coinNumber;
//                showPanel(shopPanelNew);
//                return ddnaItemUseState;
//            }
//
//            GameData.instance.useFastHint();
//
//            showQuestWarningGroup("useFasthintNum", 0);
//
//
//            if (judgeGameWin(Math.min(5, emptyBox.size()))) {
//                setInputProcessor(false);
//            }
//
//            stage.addActor(fastHintCloudSpineGroup);
//            fastHintCloudSpineGroup.setPosition(360,ViewportUtils.getTop() + 50);
//            fastHintCloudSpineGroup.setAnimation("animation",false);
//            fastHintCloudSpineGroup.setVisible(true);
//            int count = Math.min(5,emptyBox.size());
//            stage.addActor(mask);
//            mask.clearActions();
//            mask.setVisible(true);
//            mask.setColor(1,1,1,0);
//            mask.addAction(Actions.alpha(0.5F,0.1F));
//
//            Timer.schedule(new Timer.Task() {
//                @Override
//                public void run() {
//                    tPosition = emptyBox.randomGet();
//                    if (tPosition != null) {
//                        lightningAt(tPosition);
//                        lightGezi(tPosition.x, tPosition.y, true, -1);
//                    }
//                }
//            },0,0.2F,count-1);
//
//
//            stage.addAction(Actions.delay(0.2f*count + 0.1F,Actions.run(()->{
//                fastHintShow = false;
//                if (!isFind(fengPosition.index)) {
//                    getEmptyGrid(fengPosition.index);
//                } else {
//                    getEmptyGrid(-1);
//                }
//                showCombo();
//                if(!isGameWin()){
//                    setInputProcessor(true);
//                }
//            })));
//
//
//            if (addComboTimes != 0) {
//                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORD_PATH, Sound.class));
//            }
//
//        }
//
//        return ddnaItemUseState;
//
//    }
//
//    private void lightningAt(Position tPosition){
//        SpineGroup spineGroup = null;
//        SpineGroup spineGroup2 = null;
//        for(int i = 0;i<5;i++){
//            if(fastHintSpineGroup[i].getParent()==null){
//                spineGroup = fastHintSpineGroup[i];
//                break;
//            }
//        }
//
//        for(int i = 0;i<5;i++){
//            if(fastHintSpineGroup2[i].getParent()==null){
//                spineGroup2 = fastHintSpineGroup2[i];
//                break;
//            }
//        }
//
//        if(spineGroup!=null){
//            float posX = geziGroup[tPosition.x][tPosition.y].getX(Align.center);
//            float posY = geziGroup[tPosition.x][tPosition.y].getY(Align.center);
//            Vector2 tempVec = new Vector2(posX,posY);
//            geziGroup[tPosition.x][tPosition.y].getParent().localToStageCoordinates(tempVec);
//            spineGroup.setAnimation("animation",false);
//            spineGroup.setPosition(tempVec.x,tempVec.y + 360);
//            Bone bone = spineGroup.getSpineActor().getSkeleton().findBone("SanDian");
//            float boneDataScaleY = spineGroup.getSpineActor().getSkeleton().getData().findBone("SanDian").getScaleY();
//            if(ViewportUtils.getTop() - tempVec.y > 360) {
//                float scale = (ViewportUtils.getTop() - tempVec.y) / 360;
//                bone.setScaleY(boneDataScaleY*scale);
//            }else{
//                bone.setScaleY(boneDataScaleY);
//            }
//            stage.addActor(spineGroup);
//            fastHintCloudSpineGroup.toFront();
//            if(spineGroup2!=null) {
//                spineGroup2.setAnimation("animation", false);
//                spineGroup2.setPosition(tempVec.x, tempVec.y);
//                stage.addActor(spineGroup2);
//            }
//        }
//    }
//
//    SpineGroup txfankuiSpineGroup;
//    ParticleEffectActor txtwParticleEffectActor;
//    GamePlayButton hintBtn;
//    Vector2 hintBtnPos;
//
//    void hintButtonLoad() {
//
//        Group t = gameplayButtonUIEditor.createGroup();
//        int costValue;
//        costValue = Constants.hintCost;
//        hintBtn = new GamePlayButton(t, new TextureRegionDrawable(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/hint")), 1, MyEnum.PropType.hint, costValue);
//        hintBtn.setPosition(597 + ViewportUtils.getWidthOffset(), 277 - ViewportUtils.getDeltaY() / 2);
//        stage.addActor(hintBtn);
//        hintBtnPos = new Vector2(hintBtn.getX(), hintBtn.getY());
//
//        hintBtn.setNum(gameData.hintNum);
//        hintBtn.setPos(hintBtnPos);
//
//
//        hintBtn.addListener(new ButtonClickListener(false, Constants.BTN_TYPE_USEITEM) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
////				super.clicked(event, x, y);
//
//                MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//                addComboTimes = 0;
//                if (!hintBtn.getDisable() && ((yindaoPanel != null && !yindaoPanel.isShowing))) {
//
//                    playBtnSound();
//                    if (gameMode == MyEnum.GameMode.normalMode) {
//                        if (gameData.coinNumber < 119) {
//                            ddnaItemUseState = scapeHint();
//                        } else {
//                            if (gameData.useFormerHint) {
//                                ddnaItemUseState = hint();
//                            } else {
//                                ddnaItemUseState = shuffleHint();
//                            }
//                        }
//                    } else {
//                        ddnaItemUseState = hint();
//                    }
//                }
//
//
//                if (gameMode == MyEnum.GameMode.normalMode) {
//                    if (ddnaItemUseState == MyEnum.DDNAItemUseState.useFree || ddnaItemUseState == MyEnum.DDNAItemUseState.useExistingItem) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item1", 0, gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                        if (gameData.lastGetHintIs.equals("propRewardPanel")) {
//                            gameData.usePropPanelHint = true;
//                            Prefs.putBoolean("usePropPanelHint", gameData.usePropPanelHint);
//                        }
//                    } else if (ddnaItemUseState == MyEnum.DDNAItemUseState.useCoin) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item1", costValue, gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                        if (gameData.lastGetHintIs.equals("propRewardPanel")) {
//                            gameData.usePropPanelHint = true;
//                            Prefs.putBoolean("usePropPanelHint", gameData.usePropPanelHint);
//                        }
//                    }
//                } else {
//
//                    if (ddnaItemUseState == MyEnum.DDNAItemUseState.useFree || ddnaItemUseState == MyEnum.DDNAItemUseState.useExistingItem) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//                        PlatformManager.instance.itemActioned("Item1", 0, gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                        if (gameData.lastGetHintIs.equals("propRewardPanel")) {
//                            gameData.usePropPanelHint = true;
//                            Prefs.putBoolean("usePropPanelHint", gameData.usePropPanelHint);
//                        }
//                    } else if (ddnaItemUseState == MyEnum.DDNAItemUseState.useCoin) {
//                        if (gameData.dnaSetPropPanelMoreFlag) {
//                            gameData.dnaSetPropPanelNowM = 0;
//                            Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//                        }
//
//                        PlatformManager.instance.itemActioned("Item1", costValue, gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                        if (gameData.lastGetHintIs.equals("propRewardPanel")) {
//                            gameData.usePropPanelHint = true;
//                            Prefs.putBoolean("usePropPanelHint", gameData.usePropPanelHint);
//                        }
//                    }
//                }
//                addComboTimes = 0;
//            }
//        });
//
//
//        txfankuiSpineGroup = new SpineGroup(Assets.getInstance().assetManager.get(Constants.spineTxfankuiPath), new AnimationState.AnimationStateAdapter() {
//            @Override
//            public void complete(AnimationState.TrackEntry entry) {
//                if (txfankuiSpineGroup.getAnimationName().endsWith("animation")) {
//                    txfankuiSpineGroup.setVisible(false);
//                }
//            }
//        });
//        txfankuiSpineGroup.setVisible(false);
//        txfankuiSpineGroup.setTouchable(Touchable.disabled);
//        hintBtn.addActor(txfankuiSpineGroup);
//        txfankuiSpineGroup.setPosition(hintBtn.getWidth() / 2, hintBtn.getHeight() / 2);
//
//        ParticleEffect particleEffect = new ParticleEffect();
//        particleEffect.load(Gdx.files.internal(Constants.particleTxtwPath), Gdx.files.internal(Constants.particleTxtw));
//        txtwParticleEffectActor = new ParticleEffectActor(particleEffect);
//        stage.addActor(txtwParticleEffectActor);
//        txtwParticleEffectActor.setVisible(false);
//    }
//
//    //最常规的hint，22和之后习惯消费玩家使用
//    public MyEnum.DDNAItemUseState hint() {
//
//        MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//        boolean isCanHint = false;
//
//
//        for (int i = 0; i < wordCount; i++) {
//            if (!isFind(i) && hintIndex[i] < hintLength[i]) {
//                isCanHint = true;
//                break;
//            }
//        }
//
//        if (!isCanHint) {
//            return MyEnum.DDNAItemUseState.notUse;
//        }
//
//        if (gameData.hintNum > 0) {
//            gameData.updateHintNum(-1,true);
//            hintBtn.setNum(gameData.hintNum);
//            ddnaItemUseState = MyEnum.DDNAItemUseState.useExistingItem;
//        } else if (gameData.coinNumber >= hintBtn.getCoinValue()) {
//            gameData.subCoinNumber(hintBtn.getCoinValue());
//            ddnaItemUseState = MyEnum.DDNAItemUseState.useCoin;
//        } else {
//            gameData.buyLessCoin = hintBtn.getCoinValue() - gameData.coinNumber;
//            showPanel(shopPanelNew);
//            return MyEnum.DDNAItemUseState.notUse;
//        }
//
//        GameData.instance.useHint();
//        showQuestWarningGroup("useHintNum", 0);
//
//        hintId = 0;
//
//
//        isCanHint = false;
//        if (gameMode != MyEnum.GameMode.normalMode) {
//            hintId = fengPosition.index;
//            if (hintId >= 0 && !isFind(hintId) && (hintIndex[hintId] >= hintLength[hintId])) {
//                isCanHint = true;
//            }
//        }
//        while (!isCanHint) {
//            if (isFind(hintId) || (hintIndex[hintId] >= hintLength[hintId])) {
//                hintId++;
//                if (hintId == wordCount) {
//                    hintId = 0;
//                }
//                continue;
//            } else {
//                break;
//            }
//        }
//
//
//        int tx, ty, p;
//        if (hintIndex[hintId] < hintLength[hintId]) {
//            int kx, ky;
//            //水平放
//            if (wordMsg[hintId].dir == MyEnum.Dir.horizontal) {
//                kx = 0;
//                ky = 1;
//            } else {
//                kx = 1;
//                ky = 0;
//            }
//
//            for (p = hintIndex[hintId]; p < hintLength[hintId]; p++) {
//                if (gridMsg[wordMsg[hintId].x + kx * p][wordMsg[hintId].y + ky * p].appear == MyEnum.WordAppear.notAppear) {
//                    break;
//                }
//            }
//            hintIndex[hintId] = p;
//
//            tx = wordMsg[hintId].x;
//            ty = wordMsg[hintId].y;
//            int xx = tx + kx * hintIndex[hintId];
//            int yy = ty + ky * hintIndex[hintId];
//
//            if (judgeGameWin(1)) {
//                setInputProcessor(false);
//            }
//            lightGezi(xx, yy, true, -1);
//            gameData.useFormerHint = !gameData.useFormerHint;
//
//            for (p = hintIndex[hintId] + 1; p < hintLength[hintId]; p++) {
//                if (gridMsg[wordMsg[hintId].x + kx * p][wordMsg[hintId].y + ky * p].appear == MyEnum.WordAppear.notAppear)
//                    break;
//            }
//            hintIndex[hintId] = p;
//
//            //将hint的数据存储到prefs里
//            if (isNewLevel && hintIndexStr != null && !isYindaoWithoutState) {
//                gameData.hintIndex[hintId] = p;
//                Prefs.putInteger(hintIndexStr + hintId, gameData.hintIndex[hintId]);
//                Prefs.flush();
//            }
//
//
//        }
//
//        getEmptyGrid(hintId);
//        showCombo();
//
//        return ddnaItemUseState;
//    }
//
//    //scape用的hint，21关和之前使用
//    public MyEnum.DDNAItemUseState scapeHint() {
//        MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//        if (emptyBox.size() > 0) {
//            if (gameData.hintNum > 0) {
//                gameData.updateHintNum(-1,true);
//                hintBtn.setNum(gameData.hintNum);
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useExistingItem;
//            } else if (gameData.coinNumber >= hintBtn.getCoinValue()) {
//                gameData.subCoinNumber(hintBtn.getCoinValue());
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useCoin;
//            } else {
//                gameData.buyLessCoin = hintBtn.getCoinValue() - gameData.coinNumber;
//                showPanel(shopPanelNew);
//                return MyEnum.DDNAItemUseState.notUse;
//            }
//
//            GameData.instance.useHint();
//            showQuestWarningGroup("useHintNum", 0);
//
//
//            if (judgeGameWin(Math.min(1, emptyBox.size()))) {
//                setInputProcessor(false);
//            }
//
//            boolean find = false;
//            for (int i = 0; i < 7 && !find; i++) {
//                for (int j = 0; j < wordCount; j++) {
//                    if (i < wordMsg[j].length) {
//                        if (wordMsg[j].dir == MyEnum.Dir.horizontal) {
//                            int index = wordMsg[j].y + i;
//                            if (gridMsg[wordMsg[j].x][index].appear == MyEnum.WordAppear.notAppear) {
//                                lightGezi(wordMsg[j].x, index, true, -1);
//                                find = true;
//                                break;
//                            }
//                        } else {
//                            int index = wordMsg[j].x + i;
//                            if (gridMsg[index][wordMsg[j].y].appear == MyEnum.WordAppear.notAppear) {
//                                lightGezi(index, wordMsg[j].y, true, -1);
//                                find = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//
////			tPosition = emptyBox.randomGet();
////			lightGezi(tPosition.x, tPosition.y, true, -1);
//
//
//            if (!isFind(fengPosition.index)) {
//                getEmptyGrid(fengPosition.index);
//            } else {
//                getEmptyGrid(-1);
//            }
//            showCombo();
//
//
////			if (addComboTimes != 0){
////				AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORD_PATH, Sound.class));
////			}
////			if (!isFind(fengPosition.index)){
////				getEmptyGrid(fengPosition.index);
////			} else {
////				getEmptyGrid(-1);
////			}
////			showCombo();
//        }
//
//        return ddnaItemUseState;
//
//    }
//
//    //随机hint，22和之后普通玩家使用
//    public MyEnum.DDNAItemUseState shuffleHint() {
//        MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//        if (emptyBox.size() > 0) {
//            if (gameData.hintNum > 0) {
//                gameData.updateHintNum(-1,true);
//                hintBtn.setNum(gameData.hintNum);
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useExistingItem;
//            } else if (gameData.coinNumber >= hintBtn.getCoinValue()) {
//                gameData.subCoinNumber(hintBtn.getCoinValue());
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useCoin;
//            } else {
//                gameData.buyLessCoin = hintBtn.getCoinValue() - gameData.coinNumber;
//                showPanel(shopPanelNew);
//                return MyEnum.DDNAItemUseState.notUse;
//            }
//
//            GameData.instance.useHint();
//            showQuestWarningGroup("useHintNum", 0);
//
//
//            if (judgeGameWin(Math.min(1, emptyBox.size()))) {
//                setInputProcessor(false);
//            }
//
//
//            tPosition = emptyBox.randomGet();
//            lightGezi(tPosition.x, tPosition.y, true, -1);
//            gameData.useFormerHint = !gameData.useFormerHint;
//
//
//            if (!isFind(fengPosition.index)) {
//                getEmptyGrid(fengPosition.index);
//            } else {
//                getEmptyGrid(-1);
//            }
//            showCombo();
//
//
////			if (addComboTimes != 0){
////				AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_WORD_PATH, Sound.class));
////			}
////			if (!isFind(fengPosition.index)){
////				getEmptyGrid(fengPosition.index);
////			} else {
////				getEmptyGrid(-1);
////			}
////			showCombo();
//        }
//
//        return ddnaItemUseState;
//
//    }
//
//
//    void setFingerImages(boolean visible) {
//        for (int i = 0; i < wordCount; i++) {
//            int tx = wordMsg[i].x, ty = wordMsg[i].y;
//            //水平摆放
//            int wordLength = wordMsg[i].length;
//            int kx = 0, ky = 0;
//            if (wordMsg[i].dir == MyEnum.Dir.horizontal) {
//                kx = 0;
//                ky = 1;
//            } else if (wordMsg[i].dir == MyEnum.Dir.vertical) {
//                kx = 1;
//                ky = 0;
//            }
//            for (int l = 0; l < wordLength; l++) {
//                int xx = tx + kx * l;
//                int yy = ty + ky * l;
//                if (gridMsg[xx][yy].appear == MyEnum.WordAppear.notAppear && !(xx == videoBoxPos.x && yy== videoBoxPos.y)) {
//                    if (visible) {
//                        geziGroup[xx][yy].toFront();
//                        fingerImages[xx][yy].toFront();
//
//                        if (coinGeziImages[xx][yy] != null) {
//                            coinGeziImages[xx][yy].toFront();
//                        }
//                    }
//                    fingerImages[xx][yy].setVisible(visible);
//                    //这里把格子可见性给修改了
//                    geziImages[xx][yy].setVisible(!visible);
//                }
//            }
//        }
//        if (gameMode != MyEnum.GameMode.normalMode) {
//            if (warningGroup.isShowing) {
//                feng.setZIndex(warningGroup.getZIndex() - 1);
//            } else if (questWarngingGroup.isShowing) {
//                feng.setZIndex(questWarngingGroup.getZIndex() - 1);
//            } else {
//                feng.toFront();
//            }
//        }else{
//            beeFront();
//        }
//    }
//
//    private float beeScale = 1;
//
//    private void initBee(int index,int x,int y){
//        if(beePositions == null) return;
//        beePositions[index] = new Position();
//        beePositions[index].set(x,y);
//        bees[index] = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineMfPath, SkeletonData.class));
//        bees[index].setPosition(geziPos[x][y].x+geziSizeX/2, geziPos[x][y].y + geziSizeY/2);
//        bees[index].setAnimation("stop");
//        bees[index].setVisible(false);
//        beeScale = geziSizeX/120F;
//        bees[index].setScale(beeScale);
//        stage.addActor(bees[index]);
//    }
//
//
//    private void setBeePositionIndex(){
//        if(beePositions == null) return;
//        for(int i = 0;i<beePositions.length;i++){
//            Position position = beePositions[i];
//            position.set(position.x,position.y,gridMsg[position.x][position.y].hIndex,gridMsg[position.x][position.y].vIndex);
//        }
//
//    }
//
//
//    private int getBeeIndex(int x,int y){
//        if(beePositions == null) return -1;
//        for(int i = 0;i<beePositions.length;i++){
//            if(beePositions[i].x == x && beePositions[i].y == y){
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private void beeFront(){
//        if(beePositions!=null){
//            for(int i = 0;i<beePositions.length;i++){
//                int geziIndex = geziGroup[beePositions[i].x][beePositions[i].y].getZIndex();
//                int letterIndex = letter[beePositions[i].x][beePositions[i].y].getZIndex();
//                bees[i].setZIndex(Math.max(geziIndex,letterIndex)+1);
//            }
//        }
//    }
//
//
//    Image fingerMask;
//    //点击finger后出来的x
//    Image fingerX;
//    GamePlayButton fingerBtn;
//    Vector2 fingerBtnPos;
//
//    void fingerButtonLoad() {
//        fingerMask = new Image(new NinePatch(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/mask"), 1, 1, 1, 1));
//        fingerMask.setColor(fingerMask.getColor().r, fingerMask.getColor().g, fingerMask.getColor().b, 0.65f);
//        fingerMask.setSize(ViewportUtils.getWidth(), ViewportUtils.getHeight());
//        fingerMask.setPosition(ViewportUtils.getLeft(), ViewportUtils.getBottom());
//        fingerMask.setVisible(false);
//        stage.addActor(fingerMask);
//
//        fingerX = new Image(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/atlas/fingercha"));
//
//        stage.addActor(fingerX);
//        fingerX.setVisible(false);
//        fingerX.addListener(new ButtonClickListener(false, Constants.BTN_TYPE_CLICK) {
//
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                setFingerImages(false);
//                fingerMask.clearActions();
//                fingerMask.addAction(Actions.sequence(
//                        Actions.alpha(0, 0.2f)
//                        , Actions.visible(false)
//                ));
//                isFinger = false;
//                fingerX.setVisible(false);
//                textGroup.hide();
//                fingerBtn.setVisible(true);
//
//                if (gameData.gameIs == Constants.fingerStart && hasSetYindao) {
//                    hidePanel(yindaoPanel, false);
//                }
//            }
//        });
//
//        Group t = gameplayButtonUIEditor.createGroup();
//        int costValue;
//        costValue = Constants.fingerCost;
//        fingerBtn = new GamePlayButton(t, new TextureRegionDrawable(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/finger")), 1, MyEnum.PropType.finger, costValue);
//        fingerBtn.setPosition(ViewportUtils.getLeft() + 57, 105 - ViewportUtils.getDeltaY() / 2);
//        fingerX.setPosition(fingerBtn.getX(), fingerBtn.getY());
//        stage.addActor(fingerBtn);
//        fingerBtnPos = new Vector2(fingerBtn.getX(), fingerBtn.getY());
//
//
//        fingerBtn.addListener(new ButtonClickListener(false, Constants.BTN_TYPE_CLICK) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//
//                if (!fingerBtn.getDisable()) {
//                    playBtnSound();
//                    if (wordState != (1 << wordCount) - 1) {
//                        if (gameData.gameIs == Constants.fingerStart && !gameData.isYindaoed[2]) {
//                            yindaoPanel.switchAction(20, 1, actualEy - 170, null, null);
//
//                            fingerX.setVisible(true);
//                            fingerX.toFront();
//                            fingerBtn.setVisible(false);
//
//                            isFinger = true;
//                            canIgnoreMask = true;
//                            gameData.setYindao(2);
//                            setFingerImages(true);
//
//                        } else if (mask != null && !mask.isVisible()) {
//                            if (gameData.fingerNum > 0 || gameData.coinNumber >= fingerBtn.getCoinValue() || fingerBtn.isFree()) {
//                                isFinger = true;
//                                fingerMask.clearActions();
//                                fingerMask.getColor().a = 0;
//                                fingerMask.setVisible(true);
//                                fingerMask.toFront();
//                                fingerMask.addAction(Actions.alpha(0.65f, 0.3f));
//                                setFingerImages(true);
//                                fingerX.setVisible(true);
//                                fingerX.toFront();
//                                fingerBtn.setVisible(false);
//
//                                textGroup.show();
//                                textGroup.setY(actualEy - textGroup.getHeight() - 30);
//                            } else {
//                                gameData.buyLessCoin = fingerBtn.getCoinValue() - gameData.coinNumber;
//                                showPanel(shopPanelNew);
//                            }
//                        }
//                    }
//                }
//
//                addComboTimes = 0;
//
//
//            }
//        });
//        fingerSpineGroup = new SpineGroup(game.getSkeletonRenderer(), Assets.getInstance().assetManager.get(Constants.spineZimu_glow_Path, SkeletonData.class));
//        fingerSpineGroup.setTouchable(Touchable.disabled);
//        fingerSpineGroup.setOrigin(Align.center);
//        fingerSpineGroup.getSpineActor().getAnimationState().addListener(new AnimationState.AnimationStateAdapter(){
//            @Override
//            public void end(AnimationState.TrackEntry entry) {
//                super.end(entry);
//                fingerSpineGroup.remove();
//            }
//        });
//
//    }
//
//
//    //点亮x,y位置的格子，返回能连接的单词的index，已连接的不返回，水平+垂直，返回为-1不可连击
//    Vector2 lightGezi(int x, int y, boolean isHint, int flyIndex) {
//        return lightGezi(x, y, 0f, isHint, flyIndex);
//    }
//
//
//    Vector2 lightGezi(int x, int y, float delayTime, boolean isHint, int flyIndex) {
//        endVideoBox(x,y);
//        Vector2 vec = new Vector2(-1, -1);
//        if (geziGroup[x][y] == null) {
//            return null;
//        }
//
//        if (gridMsg[x][y].appear == MyEnum.WordAppear.notAppear || gridMsg[x][y].appear == MyEnum.WordAppear.fasthint) {
//
//            gridMsg[x][y].appear = MyEnum.WordAppear.hint;
//            if (isNewLevel && hintAppearStr != null && !isYindaoWithoutState) {
//                gameData.hintAppear[x][y] = true;
//                Prefs.putBoolean(hintAppearStr + x + "_" + y, true);
//                Prefs.flush();
//
//            }
//
//
//            setFingerImages(false);
//
//            if (isHint) {
//                zmcxSpineGroup[x][y].setVisible(true);
//                zmcxSpineGroup[x][y].setAnimation("animation", false);
//            }
//
//
//            int tx, ty;
//            boolean canLinkH = false;
//            //检查水平
//            if (gridMsg[x][y].hIndex != -1 && !isFindSkin(gridMsg[x][y].hIndex)) {
//                canLinkH = true;
//                tx = x;
//                ty = y - gridMsg[x][y].hPos;
//                for (int i = 0; i < gridMsg[x][y].hLength; i++) {
//                    if (gridMsg[tx][ty + i].appear == MyEnum.WordAppear.notAppear) {
//                        canLinkH = false;
//                        break;
//                    }
//                }
//            }
//            if (canLinkH) {
//                vec.x = gridMsg[x][y].hIndex;
//                setWordState(gridMsg[x][y].hIndex);
//                setSkinState(gridMsg[x][y].hIndex);
//                if (!isYindaoWithoutState) {
//                    saveWordState();
//                }
//                tx = x;
//                ty = y - gridMsg[x][y].hPos;
//                for (int j = 0; j < gridMsg[x][y].hLength; j++) {
//                    if (gridMsg[tx][ty + j].appear != MyEnum.WordAppear.empty) {
//                        gridMsg[tx][ty + j].appear = MyEnum.WordAppear.appear;
//                        addActionOnLetter(MyEnum.Dir.horizontal, tx, ty + j, j * Constants.AnimationGap * 3, geziGroup[tx][ty + j], true, false, flyIndex);
////						addActionOnLetter(MyEnum.Dir.horizontal,tx, ty + j, 0, geziGroup[tx][ty + j], true, false, flyIndex);
//                    }
//                }
//            }
//
//
//            //检查垂直
//            boolean canLinkV = false;
//            if (gridMsg[x][y].vIndex != -1 && !isFindSkin(gridMsg[x][y].vIndex)) {
//                canLinkV = true;
//                tx = x - gridMsg[x][y].vPos;
//                ty = y;
//                for (int i = 0; i < gridMsg[x][y].vLength; i++) {
//                    if (gridMsg[tx + i][ty].appear == MyEnum.WordAppear.notAppear) {
//                        canLinkV = false;
//                        break;
//                    }
//                }
//            }
//            if (canLinkV) {
//                vec.y = gridMsg[x][y].vIndex;
//                setWordState(gridMsg[x][y].vIndex);
//                setSkinState(gridMsg[x][y].vIndex);
//                if ( !isYindaoWithoutState) {
//                    saveWordState();
//                }
//
//                tx = x - gridMsg[x][y].vPos;
//                ty = y;
//                for (int j = 0; j < gridMsg[x][y].vLength; j++) {
//                    if (gridMsg[tx + j][ty].appear != MyEnum.WordAppear.empty) {
//                        gridMsg[tx + j][ty].appear = MyEnum.WordAppear.appear;
//                        addActionOnLetter(MyEnum.Dir.vertical, tx + j, ty, j * Constants.AnimationGap * 3, geziGroup[tx + j][ty], true, false, flyIndex);
////						addActionOnLetter(MyEnum.Dir.vertical,tx + j, ty, 0, geziGroup[tx+j][ty], true, false, flyIndex);
//                    }
//                }
//            }
//
//            if (!canLinkH && !canLinkV) {
//                addActionOnLetter(MyEnum.Dir.other, x, y, letter[x][y], isHint, flyIndex);
//                beeFly(getBeeIndex(x,y),0);
////				if (delayTime == 0){
////					addActionOnLetter(MyEnum.Dir.other,x, y, letter[x][y], isHint, isFly);
////				} else {
////					addActionOnLetter(MyEnum.Dir.other,x, y,delayTime, letter[x][y], false, isHint, isFly);
////				}
//            }
//
//        }
//
//        return vec;
//    }
//
//
//    MyEnum.DDNAItemUseState fingerhint() {
//        MyEnum.DDNAItemUseState ddnaItemUseState = MyEnum.DDNAItemUseState.notUse;
//        int x = findLetterX, y = findLetterY;
//        if (x < 0 || y < 0) {
//            return MyEnum.DDNAItemUseState.notUse;
//        }
//        if (gridMsg[x][y].appear == MyEnum.WordAppear.notAppear) {
//            //扣钱或者扣数
//            if (gameData.gameIs == Constants.fingerStart && canIgnoreMask || fingerBtn.isFree()) {
//                if(gameData.dnaSetOutput){
//                    //这里将第三关同时忽视遮罩默认为是引导的时候
//                    //当是第4关的引导的隐藏引导面板
//                    hidePanel(yindaoPanel, false);
//                    Prefs.putBoolean("isFingerFree", false);
//                    GameData.instance.updateFingerNum(-1,false);
//                    fingerBtn.setNum(gameData.fingerNum);
//                    canIgnoreMask = false;
//                    ddnaItemUseState = MyEnum.DDNAItemUseState.useFree;
//                }else{
//                    //这里将第三关同时忽视遮罩默认为是引导的时候
//                    //当是第4关的引导的隐藏引导面板
//                    hidePanel(yindaoPanel, false);
//                    Prefs.putBoolean("isFingerFree", false);
//                    GameData.instance.updateFingerNum(1,false);
//                    fingerBtn.setNum(gameData.fingerNum);
//                    canIgnoreMask = false;
//                    ddnaItemUseState = MyEnum.DDNAItemUseState.useFree;
//                }
//            } else if (gameData.fingerNum > 0) {
//                GameData.instance.updateFingerNum(-1,true);
//                fingerBtn.setNum(gameData.fingerNum);
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useExistingItem;
//            } else if (gameData.coinNumber >= fingerBtn.getCoinValue()) {
//                gameData.subCoinNumber(fingerBtn.getCoinValue());
//                ddnaItemUseState = MyEnum.DDNAItemUseState.useCoin;
//            } else {
//                gameData.buyLessCoin = fasthintBtn.getCoinValue() - gameData.coinNumber;
//                showPanel(shopPanelNew);
//                return MyEnum.DDNAItemUseState.notUse;
//            }
//
//            GameData.instance.useFinger();
//
//            showQuestWarningGroup("useFingerNum", 0);
//
//
//            setFingerImages(false);
//            fingerImages[x][y].setVisible(false);
//
//
//            fingerMask.clearActions();
//            fingerMask.addAction(Actions.sequence(
//                    Actions.alpha(0, 0.2f)
//                    , Actions.visible(false)
//            ));
////			fingerMask.setVisible(false);
//            isFinger = false;
//            fingerX.setVisible(false);
//            textGroup.hide();
//            fingerBtn.setVisible(true);
//
//
//            if (judgeGameWin(1)) {
//                setInputProcessor(false);
//            }
//            lightGezi(x, y, true, -1);
//
//            float posX = geziGroup[x][y].getX(Align.center);
//            float posY = geziGroup[x][y].getY(Align.center);
//            Vector2 tempVec = new Vector2(posX,posY);
//            geziGroup[x][y].getParent().localToStageCoordinates(tempVec);
//            fingerSpineGroup.setPosition(tempVec.x,tempVec.y);
//            fingerSpineGroup.getSpineActor().clearAnimation();
//            stage.addActor(fingerSpineGroup);
//            fingerSpineGroup.setAnimation("animation",false);
//            AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_USEITEM_PATH, Sound.class), 0.7f);
//            getEmptyGrid(fengPosition.index);
//
//            showCombo();
//        }
//
//        return ddnaItemUseState;
//    }
//
//    GamePlayButton shuffleBtn;
//    Vector2 shuffleBtnPos;
//
//    void shuffleButtonLoad() {
//
//
//        Group t = gameplayButtonUIEditor.createGroup();
//        shuffleBtn = new GamePlayButton(t, new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/gameplaybutton/shuffle"))), 1, MyEnum.PropType.shuffle, 0);
//        shuffleBtn.setPosition(552 + ViewportUtils.getWidthOffset(), 105 - ViewportUtils.getDeltaY() / 2);
//        stage.addActor(shuffleBtn);
//
//        shuffleBtnPos = new Vector2(shuffleBtn.getX(), shuffleBtn.getY());
//
//
//        shuffleBtn.addListener(new ButtonClickListener(false, Constants.BTN_TYPE_CLICK) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                //notice:不用使用
////				for (int i = 0; i < wordCount; i ++){
////					if (!isFind(i)){
////						setWordFly(i);
////						break;
////					}
////				}
//
//
//                if (!shuffleBtn.getDisable() && ((yindaoPanel != null && !yindaoPanel.isShowing) || (yindaoPanel == null))) {
//                    shuffleLetters();
//                }
//
//                if (gameMode == MyEnum.GameMode.normalMode) {
//                    PlatformManager.instance.itemActioned("Item4", 0, gameData.gameSolved, gameData.coinNumber, "normal", gameData.DDNALevelText);
//                } else {
//                    PlatformManager.instance.itemActioned("Item4", 0, gameData.gameSolved, gameData.coinNumber, "challenge", gameData.DDNALevelText);
//                }
//
//
//            }
//        });
//    }
//
//    @Override
//    public void showWarningGroup(String text) {
//        if(fastHintShow) return;
//        super.showWarningGroup(text);
//    }
//
//    @Override
//    public void showQuestWarningGroup(String name, int type) {
//        if(fastHintShow) return;
//        super.showQuestWarningGroup(name, type);
//    }
//
//    SpineGroupButton videoBtn;
//    private boolean videoBtnClosed = true;
//    private Vector2 videoBtnPos = new Vector2();
//
//    public void setVideoBtnClosed(boolean flag){
//        if(!videoBtn.getCanVisible()) return;
//        if(videoBtnClosed == flag) return;
//        this.videoBtnClosed = flag;
//        if(videoBtnClosed){
//            videoBtn.setTouchable(Touchable.disabled);
//            videoBtn.setAnimation("3",false);
//            videoBtn.getSpineGroup().getSpineActor().getAnimationState().setTimeScale(2);
//            videoBtn.getSpineGroup().getSpineActor().getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
//                @Override
//                public void complete(AnimationState.TrackEntry entry) {
//                    super.complete(entry);
//                    videoBtn.setVisible(false);
//                    videoBtn.getSpineGroup().getSpineActor().getAnimationState().clearListeners();
//                }
//            });
//        }else{
//            videoBtn.getSpineGroup().getSpineActor().getAnimationState().setTimeScale(1.2F);
//            videoBtn.setVisible(true);
//            videoBtn.setTouchable(Touchable.enabled);
//            videoBtn.setColor(1,1,1,1);
//            videoBtn.setAnimation("1",false);
//        }
//    }
//
//    private ZoomButton spellingBeeBtn;
//    private Vector2 spellingBeeBtnPos;
//    private Label spellBeeLbl;
//
//    void spellBeeButtonLoad() {
//        if(GameData.instance.gameMode != MyEnum.GameMode.normalMode) return;
//        if(!(GameData.instance.spellingBeeOpen || GameData.instance.spellingBees>0)) return;
//
//        Group t = Assets.getInstance().assetManager.get(Constants.spellingBeeBtnPath,ManagerUIEditor.class).createGroup();
//        spellBeeLbl = t.findActor("lbl");
//        spellBeeLbl.setText(GameData.instance.spellingBees+"");
//        spellingBeeBtn = new ZoomButton(t,2,"spellBeeBtn");
//        spellingBeeBtn.setOrigin(Align.center);
//        spellingBeeBtn.setPosition(ViewportUtils.getLeft() + 60, 424 - ViewportUtils.getDeltaY() / 2,Align.bottom);
//        stage.addActor(spellingBeeBtn);
//        spellingBeeBtnPos = new Vector2(spellingBeeBtn.getX(), spellingBeeBtn.getY());
//        spellingBeeBtn.addListener(new ButtonClickListener(true, Constants.BTN_TYPE_CLICK) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
////				super.clicked(event, x, y);
//                spellingBeePanel.setTrigger("view");
//                showPanel(spellingBeePanel);
//            }
//        });
//    }
//
//
//
//    void videoButtonLoad() {
//        SpineGroup tempSpineGroup = new SpineGroup(Constants.videoBoxPath);
//        videoBtn = new SpineGroupButton(tempSpineGroup, "3", 130, 1);
//        videoBtn.getSpineGroup().getSpineActor().getAnimationState().setTimeScale(1.5F);
//        videoBtnPos.set(605 + ViewportUtils.getWidthOffset(),424 - ViewportUtils.getDeltaY() / 2);
//        videoBtn.setPosition(videoBtnPos.x, videoBtnPos.y);
//        videoBtn.getSpineGroup().getSpineActor().getAnimationState().setTimeScale(1.2F);
//        stage.addActor(videoBtn);
//        videoBtn.addListener(new ButtonClickListener(true, Constants.BTN_TYPE_CLICK) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
////				super.clicked(event, x, y);
//                PlatformManager.instance.outPut("ADS_new", "InspireButton", "true");
//                boolean videoReady = PlatformManager.instance.isRewaedVideoReady();
//                if (videoReady) {
//                    showPanel(videoRewardPanel);
//                } else if (!PlatformManager.instance.isNetworkAvailable()) {
//                    playBtnSound();
//                    showWarningGroup("No internet connection!");
//                } else {
//                    playBtnSound();
//                    showWarningGroup("The video is loading ...");
//                }
//            }
//        });
//        videoBtn.setColor(1,1,1,0);
//        videoBtn.setTouchable(Touchable.disabled);
//    }
//
//    boolean dicBtnCanVisible = true;
//
//    public Group getDictBtn() {
//        return dictBtn;
//    }
//
//    ZoomButton dictBtn;
//
//    void dictBtnLoad() {
//
//        dictBtn = new ZoomButton((Group) baseGroup.findActor("dicBtn"), 2, "dicBtn"){
//            @Override
//            public void setVisible(boolean visible) {
//                super.setVisible(visible);
//            }
//        };
//        dictBtn.setOrigin(Align.center);
//        dictBtn.setPosition(491, ViewportUtils.getTop() - 23, Align.top);
//        dictBtn.setX(dictBtn.getX() + ViewportUtils.getWidthOffset());
//        stage.addActor(dictBtn);
//        dictBtn.addListener(new ButtonClickListener(true, Constants.BTN_TYPE_CLICK) {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                if (allWordPanel != null && allWordPanel.isInit) {
//                    if (allWordPanel.isShowing) {
//                        PlatformManager.instance.hidePanel(allWordPanel);
//                    } else {
//                        showPanel(allWordPanel);
//                        dictBtn.toFront();
//                        PlatformManager.instance.outPut("WordClick", "button", "" + useToday);
//                    }
//                }
//            }
//        });
//    }
//
//    public void shuffleLetters() {
//        int len = letterLength;
//        shuffle(len);
//        float moveTime = 0.15f;
//        float delayTime = 0.2f;
//
//
//        for (int i = 0; i < len; i++) {
//            bottomLettersGroup[i].addAction(Actions.sequence(Actions.parallel(Actions.moveTo(pinGroup.getOriginX() - bottomLettersGroup[0].getWidth() / 2f, pinGroup.getOriginY() - bottomLettersGroup[0].getHeight() / 2f, moveTime, Interpolation.pow2Out), Actions.alpha(0f, moveTime, Interpolation.pow2Out))
//                    , Actions.delay(delayTime)
//                    , Actions.parallel(Actions.moveTo(getPositionXByRank(rank[i], len), getPositionYByRank(rank[i], len), moveTime, Interpolation.pow2Out), Actions.alpha(1, moveTime, Interpolation.pow2Out))
//            ));
//
//            woodImage[i].addAction(Actions.sequence(Actions.parallel(Actions.moveTo(pinGroup.getOriginX() - bottomLettersGroup[0].getWidth() / 2f, pinGroup.getOriginY() - bottomLettersGroup[0].getHeight() / 2f, moveTime, Interpolation.pow2Out), Actions.alpha(0f, moveTime, Interpolation.pow2Out))
//                    , Actions.delay(delayTime)
//                    , Actions.parallel(Actions.moveTo(getPositionXByRank(rank[i], len), getPositionYByRank(rank[i], len), moveTime, Interpolation.pow2Out), Actions.alpha(1, moveTime, Interpolation.pow2Out))
//            ));
//
//        }
//
//
////		pin.setOrigin(Align.center);
////		pin.setDebug(true);
////		Vector2 vec = pinGroup.localToStageCoordinates(new Vector2(pin.getOriginX(), pin.getOriginY()));
////		for (int i = 0; i < len; i ++){
////			Vector2 bottomeLetterVec = new Vector2(0, 0);
////			bottomLettersGroup[i].localToStageCoordinates(bottomeLetterVec);
////			bottomLettersGroup[i].setOrigin(vec.x - bottomeLetterVec.x, vec.y - bottomeLetterVec.y);
////			System.out.println(bottomLettersGroup[i].getOriginX() + "   " + bottomLettersGroup[i].getOriginY());
////			bottomLettersGroup[i].addAction(Actions.sequence(Actions.rotateBy(-360, 0.2f)));
//////			bottomLettersGroup[i].setDebug(true);
////		}
//
//
//    }
//
//
//    //收集x,y格子的金币
//    void GetcoinWord(int x, int y) {
//        if (coinGeziImages[x][y] != null) {
//            coinGeziImages[x][y].setVisible(false);
//            gegSpineGroup[x][y].setVisible(false);
//            addCoinsWithParticle(1, coinGeziImages[x][y].getWidth(), coinGeziImages[x][y].getHeight(), coinGeziImages[x][y].getX(), coinGeziImages[x][y].getY(), 1);
//        }
//    }
//
//
//    @Override
//    public void rewardVideoSkip() {
//        super.rewardVideoSkip();
//
//
//        if (gameData.rewardVideoState == MyEnum.RewardVideoState.LevelPassPanelRewardVideoState) {
//            levelPassPanel.setTouchable(Touchable.enabled);
//            if (!PlatformManager.instance.isRewaedVideoReady()) {
//                levelPassPanel.findActor("watchBtn").setVisible(false);
//            }
//        } else if (gameData.rewardVideoState == MyEnum.RewardVideoState.GetHintBtnRewardVideoState) {
//            Panel panel = getPanel("PropRewardPanel");
//            if (panel != null && panel.isShowing) {
//                ((PropRewardPanel) panel).setSkipState();
//            }
//        }else{
//        }
//    }
//
//    @Override
//    public void rewardVideoSuccess() {
//
//        gameData.questWatchVideoNum++;
//        Prefs.putInteger("questWatchVideoNum", gameData.questWatchVideoNum);
//
//        for (int k0 = 0; k0 < gameData.dailyQuestId.length; k0++) {
//            if (gameData.dailyQuestId[k0] == 8) {
//                if (k0 == 2 && gameData.gameSolved < 85) {
//                    break;
//                }
//                gameData.dailyquestWatchVideoNum++;
//                Prefs.putInteger("dailyquestWatchVideoNum", gameData.dailyquestWatchVideoNum);
//                break;
//            }
//        }
//
//
//        Prefs.flush();
//
//        if (gameData.rewardVideoState == MyEnum.RewardVideoState.AdPanelRewardVideoState) {
////            addCoinsWithParticle(67, 67, videoRewardPanelCoin.coinVec.x, videoRewardPanelCoin.coinVec.y, gameData.rewardVideoAddCoinNum);
////            //adpanel看广告回来
////            videoRewardPanelCoin.videoRewardSuccess();
////            mask.addAction(Actions.sequence(Actions.delay(1), Actions.run(new Runnable() {
////                public void run() {
////                    hidePanel(videoRewardPanelCoin, false);
////                }
////            })));
////            showQuestWarningGroup("questWatchVideoNum", 0);
////            showQuestWarningGroup("dailyquestWatchVideoNum", 1);
//
//        } else if (gameData.rewardVideoState == MyEnum.RewardVideoState.LevelPassPanelRewardVideoState) {
//            gameData.canClickNextGame = false;
//            //通关面板
//            addCoinsWithParticle(levelPassPanel.coinVec.x, levelPassPanel.coinVec.y, gameData.rewardVideoAddCoinNum);
//            stage.addAction(Actions.sequence(Actions.delay(1.5f), Actions.run(()->{
//                gameData.gameIs++;
//                if (levelPassPanel != null && levelPassPanel.isInit) {
//                    levelPassPanel.setGameJumpFlag();
//                }
//            })));
//        } else if (gameData.rewardVideoState == MyEnum.RewardVideoState.GetHintBtnRewardVideoState) {
//            //propRewardPanel看广告回来
//            PlatformManager.instance.hintVideo(gameData.gameSolved, "getHint", gameData.DDNALevelType, gameData.DDNALevelText);
//            propRewardPanel.showHintAnimation();
//            txtwParticleEffectActor.setPosition(propRewardPanel.yellowSpineActorGroupOrigin.x, propRewardPanel.yellowSpineActorGroupOrigin.y);
//
//            float x1 = propRewardPanel.yellowSpineActorGroupOrigin.x;
//            float y1 = propRewardPanel.yellowSpineActorGroupOrigin.y;
//            float x4 = hintBtn.getX() + hintBtn.getWidth() / 2;
//            float y4 = hintBtn.getY() + hintBtn.getHeight() / 2;
//
//            float x2 = (x1 + x4) / 2 + 100;
//            float y2 = (y1 + y4) / 2 - 30;
//
//            BezierMoveAction bezierMoveAction = new BezierMoveAction();
//            bezierMoveAction.setBezier(x1, y1, x2, y2, x2, y2, x4, y4);
//            bezierMoveAction.setDuration(0.6f);
//            txtwParticleEffectActor.toFront();
//            txtwParticleEffectActor.reset();
//            txtwParticleEffectActor.setVisible(true);
//
//            txtwParticleEffectActor.addAction(Actions.sequence(Actions.visible(true)
//                    , bezierMoveAction
////					, Actions.moveTo(hintBtn.getX() + hintBtn.getWidth()/2, hintBtn.getY() + hintBtn.getHeight()/2, 0.5f)
//                    , Actions.run(()->{
//                        txfankuiSpineGroup.setAnimation("animation", false);
//                        txfankuiSpineGroup.setVisible(true);
//                        hintBtn.setNum(gameData.hintNum);
//                    })
//                    , Actions.visible(false)
//                    , Actions.moveTo(propRewardPanel.yellowSpineActorGroupOrigin.x, propRewardPanel.yellowSpineActorGroupOrigin.y)));
//
//
//            showQuestWarningGroup("questWatchVideoNum", 0);
//            showQuestWarningGroup("dailyquestWatchVideoNum", 1);
//        } else if(gameData.rewardVideoState == MyEnum.RewardVideoState.VideoRewardPanelRewardVideoState){
//            if(videoRewardPanel.isShowing){
//                videoRewardPanel.rewardVideoSuccess();
//            }
//        }else if(gameData.rewardVideoState == MyEnum.RewardVideoState.VideoGameBoxState){
//            GameData.instance.lightningNum = 1;
//            getMoreStike();
//        }else if(gameData.rewardVideoState == MyEnum.RewardVideoState.VideoGameBoxState3Times){
//            GameData.instance.lightningNum = 3;
//            hidePanel(strikePanel,false);
////            getMoreStike();
//        }
//    }
//
//    private void getMoreStike(){
//        if(PlatformManager.instance.isRewaedVideoReady()) {
//            showPanel(strikePanel);
//        }else{
//            GameData.instance.lightningNum = 1;
//            videoBox.setAnimation("animation3");
//            lightningAt(videoBoxPos);
//            lightGezi(videoBoxPos.x, videoBoxPos.y, true, -1);
//            GameData.instance.lightningNum = 0;
//        }
////        videoBox.setAnimation("animation3");
////        lightningAt(videoBoxPos);
////        lightGezi(videoBoxPos.x, videoBoxPos.y, true, -1);
//    }
//
//
//
//    @Override
//    public void onBack() {
//        if (isYindaoWithoutState) {
//            return;
//        }
//        if(yindaoPanel.isShowing) return;
//        if (panels.size == 0) {
//            if (fingerX.isVisible()) {
//                //当时手指引导模式的时候
//                setFingerImages(false);
//                fingerMask.clearActions();
//                fingerMask.addAction(Actions.sequence(
//                        Actions.alpha(0, 0.2f)
//                        , Actions.visible(false)
//                ));
//                textGroup.hide();
//                isFinger = false;
//                fingerX.setVisible(false);
//                fingerBtn.setVisible(true);
//            } else if (gameMode == MyEnum.GameMode.normalMode) {
//                gameplayGotoScreen(()->{
//                    gameData.startScreenAlpha = true;
//                    PlatformManager.instance.gotoScreen("StartScreen");
//                });
//            }else if(gameMode == MyEnum.GameMode.dailyMode){
//                showPanel(dailyPanel);
//            }  else {
//                gameplayGotoScreen(()->{
//                    PlatformManager.instance.gotoScreen("StartScreen");
//                });
//            }
//        } else {
//            if (gameMode == MyEnum.GameMode.normalMode && ((levelPassPanel !=null&& levelPassPanel.isShowing) || novicesRewardPanel.isShowing )) {
//                return;
//            } else if (gameMode != MyEnum.GameMode.normalMode && panels.size == 1 && dailyPanel.isShowing) {
//                dailyPanel.back();
//            } else if (yindaoPanel.isShowing) {
//                return;
//            }else {
//                PlatformManager.instance.hidePanel(panels.get(panels.size - 1));
//            }
//        }
//
//    }
//
//    @Override
//    public void showPanel(Panel panel) {
//        if(panel == null) return;
//        if (!panel.isShowing) {
//            if (!panel.isDialog) {
//                float delayTime = setHideAction(true);
//                if(panel instanceof LevelPassPanel){
//                    delayTime-=0.15F;
//                    dictBtn.clearActions();
//                    dictBtn.setColor(1,1,1,1);
//                }
//
//                if(isGameWin() && panel instanceof DailyPanel){
//                    dailyProgress.clearActions();
//                }
//                runDelay(()->{
//                    if(panel instanceof DailyPanel && ((DailyPanel)panel).isGameEnd()){
//                        PlatformManager.instance.showInterstitialAd();
//                        GameData.instance.showInterstitialAd = false;
//                    }
//                    showPanelGo(panel);
//                }, delayTime);
//            } else {
//                showPanelGo(panel);
//            }
//
//        }
//    }
//
//    @Override
//    public void hidePanel(Panel panel, boolean isJustHide) {
//        if (panel == null) {
//            return;
//        }
//        if (panel.isShowing) {
//            //这里比较内存地址就行，不用比较值
//            if(!(panel instanceof LevelPassPanel || panel instanceof DailyPanel)){
//                panels.removeValue(panel, false);
//            }
//            if (isJustHide) {
//                panel.justHide();
//            } else {
//                panel.hide();
//            }
//            if(panel instanceof StrikePanel){
//                strikePanelHide();
//            }
//
//            if (panels.size == 0) {
//                showMask(true,Math.max(panel.panelHideTime-0.2F,0),0);
//                //最后一个推出的面板是关卡面板不让各自出来
//                runDelay(()->{
//                    setAppearAction();
//                }, panel.panelHideTime);
//            } else {
//                Panel topPanel = panels.peek();
//                if (!topPanel.isVisible()) {
//                    panels.peek().show();
//                }else{
//                    if(panel.isDialog ){
//                        if(topPanel.isDialog) {
//                            showMask(true, Math.max(panel.panelHideTime-0.2F,0), 0.5F);
//                        }else{
//                            showMask(true, Math.max(panel.panelHideTime-0.2F,0), Math.max(maskTargetAlpha - 0.5F, 0));
//                        }
//                    }
//                }
//            }
//        }
//        update();
//    }
//
//
//    private void strikePanelHide(){
//        videoBoxUsed = true;
//        setInputProcessor(false);
//        stage.addAction(Actions.delay(0.2F,Actions.run(()->{
//            setInputProcessor(true);
//            if(GameData.instance.lightningNum == 1){
//                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_USEITEM_PATH, Sound.class));
//                videoBox.setAnimation("animation3");
//                lightningAt(videoBoxPos);
//                lightGezi(videoBoxPos.x, videoBoxPos.y, true, -1);
//            }else{
//                AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_USEITEM_PATH, Sound.class));
//                videoBox.setAnimation("animation3");
//                lightningAt(videoBoxPos);
//                lightGezi(videoBoxPos.x, videoBoxPos.y, true, -1);
//                setInputProcessor(false);
//                int count = Math.min(2,emptyBox.size());
//                Timer.schedule(new Timer.Task() {
//                    @Override
//                    public void run() {
//                        tPosition = emptyBox.randomGet();
//                        if (tPosition != null) {
//                            AudioManager.playSound(Assets.getInstance().assetManager.get(Constants.SFX_USEITEM_PATH, Sound.class));
//                            lightningAt(tPosition);
//                            lightGezi(tPosition.x, tPosition.y, true, -1);
//                        }
//                    }
//                },0.2F,0.2F,count-1);
//
//                stage.addAction(Actions.delay(0.2F * (count+1),Actions.run(()->{
//                    if(!isGameWin()){
//                        setInputProcessor(true);
//                    }
//                })));
//
//            }
//            GameData.instance.lightningNum = 0;
//        })));
//
//
//    }
//
//
//    @Override
//    public void render(float delta) {
//        if (!gameData.hasLoadJigsaw) {
//            //载入拼图资源
//            if (Assets.getInstance().assetManager.update()) {
//                gameData.hasLoadJigsaw = true;
//            }
//        }
//
//        propRewardPanelShowTime += delta;
//        gameData.interstitialAdTime += delta;
//        gameData.errorGameTime += delta;
//        gameData.DDNATimeCount += delta;
//
//        if (!hasPanelShowing() && !isLianxian) {
//            gameData.DDNAOperationIntervalTime += delta;
//            gameData.DDNAerrorBeforeFindWord += delta;
//        }
//
//        if (isLianxian) {
//            //连线的时候增加连词时间
//            gameData.DDNALianciTime += delta;
//        }
//
//        updateNoTouchTime(delta);
//        if(debug && gameMode == MyEnum.GameMode.normalMode) {
////            info[0].setText("Total Coin Cost: " + (GameData.instance.useHintNum * 60 + GameData.instance.useFingerNum * 120 + GameData.instance.useFasthintNum * 180));
////            info[1].setText("Total Feel Hard: " + GameData.instance.feelHardCount);
////            info[2].setText("Level Coin Cost: " + GameData.instance.levelCost);
////            info[3].setText("Level Feel Hard: " + GameData.instance.levelFeelHarCount);
////            if (GameData.instance.feelHardCount != 0) {
////                info[4].setText("Total Hard Rate: " + (GameData.instance.useHintNum * 60 + GameData.instance.useFingerNum * 120 + GameData.instance.useFasthintNum * 180) / (float) GameData.instance.feelHardCount);
////            } else {
////                info[4].setText("Total Hard Rate: 0");
////            }
////            info[0].setText("AvgLev: " + PopupManager.getInstance().toString());
////            info[1].setText("");
////            info[2].setText("");
////            info[3].setText("");
////            info[4].setText("");
////            info[5].setText("");
////            info[6].setText("");
////            info[7].setText("");
////            info[2].setText("probability id: " + UserProbability.instance.id);
////            info[3].setText("DailyCrown: " + UserProbability.instance.DailyCrown);
////            info[4].setText("VideoPush: " + UserProbability.instance.VideoPush);
////            info[5].setText("ChapterGift: " + (UserProbability.instance.ChapterGift+1));
////            info[6].setText("VideoGift: " + (UserProbability.instance.VideoGift+1));
////            info[7].setText("LoadGift: " + (UserProbability.instance.LoadGift+1));
//
//
////            //概率测试
////
////            info[0].setText("hasPurchase: " + GameData.instance.hasPurchase);
////            info[1].setText("probability id: " + UserProbability.instance.id);
////            info[2].setText("DailyCrown: " + UserProbability.instance.DailyCrown);
////            info[3].setText("VideoPush: " + UserProbability.instance.VideoPush);
////            info[4].setText("ChapterGift: " + (UserProbability.instance.ChapterGift+1));
////            info[5].setText("VideoGift: " + (UserProbability.instance.VideoGift+1));
////            info[6].setText("ddnaId: " + GameData.instance.ddnaID );
////            info[7].setText("usingId: " +  PlatformManager.instance.getDDNAId() );
//
//            info[0].setText("dnaSetOutput: " + GameData.instance.dnaSetOutput);
//            info[1].setText("dnaSetEventbee: " + GameData.instance.dnaSetEventbee);
//            info[2].setText("DailyCrown: " + UserProbability.instance.DailyCrown);
//            info[3].setText("VideoPush: " + UserProbability.instance.VideoPush);
//            info[4].setText("ChapterGift: " + (UserProbability.instance.ChapterGift+1));
//            info[5].setText("VideoGift: " + (UserProbability.instance.VideoGift+1));
//            info[6].setText("ddnaId: " + GameData.instance.ddnaID );
//            info[7].setText("usingId: " +  PlatformManager.instance.getDDNAId() );
//
//
////            info[5].setText("time " + RuntimeData.instance.gameTime + " Read: " + LevelUtil.getRfmTime(gameIs) + " Rfm: " + GameData.instance.timeCostRfm +  "   " +(float)GameData.instance.timeCostRfm/GameData.instance.timeCostRfmCount);
////            info[6].setText("cost " + GameData.instance.chapterCost + " Read: " + LevelUtil.getRfmCost(GameData.instance.chapterIs) + " Rfm: " + GameData.instance.itemUsedRfm +  "   " +(float)GameData.instance.itemUsedRfm/GameData.instance.itemUsedRfmCount);
////            info[6].setText("Has Make Sure Panel " + Prefs.containsKey(Constants.PopupInfo_MakeSure) + "   Panel exibts?  " + Prefs.containsKey(Constants.PopupInfo_condition));
////            info[7].setText("rfm : " + PopupManager.getInstance().getRfmNum() + "    Is in CD " + (makeSure() == null));
//        }
////        if(GameData.instance.levelFeelHarCount!=0) {
////            info[4].setText("Level Hard Rate: " + GameData.instance.levelCost / (float)GameData.instance.levelFeelHarCount);
////        }
//        super.render(delta);
//    }
//
//    public Object makeSure(){
//        PythonDict cdInfoDict = PopupManager.getInstance().getCDInfo();
//        if (cdInfoDict != null) {
//            long cdStartTime = ConvertUtil.convertToLong(cdInfoDict.get("CDStartTime"), 0);
//            long cdTime = ConvertUtil.convertToLong(cdInfoDict.get("CDTime"), 0);
//            if (System.currentTimeMillis() < cdStartTime + cdTime * 60 * 60 * 1000) {
//                return null;
//            }
//        }
//        return new Object();
//    }
//
//
//    float noTouchTime = 0;
//
//    void updateNoTouchTime(float delta) {
//        if (isWordYindao) {
//            return;
//        }
//        if (mask != null && mask.isVisible()) {
//            noTouchTime = 0;
//            return;
//        }
//        noTouchTime += delta;
//
//        if (noTouchTime >= 15f) {
//            noTouchTime = 0;
//            double ra = Math.random();
//
//            if (ra < 0.5f) {
//                //按钮晃动
//                addShakeAction(hintBtn);
//            } else {
//                //单词晃动
//                for (int i = 0; i < wordCount; i++) {
//                    if (!isFind(i)) {
//                        int[] pos = wordToGrid(i);
//                        for (int j = 0; j < pos.length / 2; j++) {
//                            addShakeAction(geziGroup[pos[2 * j]][pos[2 * j + 1]]);
//                            addShakeAction(letter[pos[2 * j]][pos[2 * j + 1]]);
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    //关卡退出
//    public void levelExit() {
//        if (!isJiesuan) {
//            if (gameMode == MyEnum.GameMode.dailyMode || gameData.dailyPass) {
//                PlatformManager.instance.levelExit("daily", gameData.DDNALevelText, gameData.DDNADailyStartRecord, 0, gameData.DDNAErrorCount, gameData.DDNATimeCount, gameData.DDNABonusCount, gameData.coinNumber, gameData.allFengNum, true, DDNALeverVersion,"null",0,0);
//            } else {
//                if (isNewLevel) {
//                    PlatformManager.instance.levelExit("normal", gameData.DDNALevelText, gameData.DDNAStartRecord, 0, gameData.DDNAErrorCount, gameData.DDNATimeCount, gameData.DDNABonusCount, gameData.coinNumber, gameData.allFengNum, false, DDNALeverVersion,"null",0,0);
//                } else {
//                    PlatformManager.instance.levelExit("normal", gameData.DDNALevelText, 1, 0, gameData.DDNAErrorCount, gameData.DDNATimeCount, gameData.DDNABonusCount, gameData.coinNumber, gameData.allFengNum, false, DDNALeverVersion,beePositions!=null?"spellingBee":"null",maxBees,levelBees);
//                }
//            }
//
//            if (gameData.DDNAOperationIntervalSaveTime.length() != 0) {
//                Prefs.putInteger("changeMayFeelHardSaveIndex", gameData.changeMayFeelHardSaveIndex);
//                Prefs.putString("DDNAOperationIntervalSaveTime" + gameData.changeMayFeelHardSaveIndex, gameData.DDNAOperationIntervalSaveTime.toString());
//                Prefs.flush();
//            }
//
//            if (gameData.DDNAOperationInvalidSaveWord.length() != 0) {
//                Prefs.putInteger("changeMayFeelHardSaveIndex", gameData.changeMayFeelHardSaveIndex);
//                Prefs.putString("DDNAOperationInvalidSaveWord" + gameData.changeMayFeelHardSaveIndex, gameData.DDNAOperationInvalidSaveWord.toString());
//                Prefs.flush();
//            }
//        }
//    }
//
//    @Override
//    public void dispose() {
//        super.dispose();
//
//        levelExit();
//
//        gameData.dailyPass = false;
//        for (String word : Assets.getInstance().wordAudioAssetPath) {
//            String path = "word/audio/" + word + ".mp3";
//            if (Assets.getInstance().localAssetManager.contains(path)) {
//                //删除读音
//                Assets.getInstance().localAssetManager.unload(path);
//            }
//        }
//
//        if (!PlatformManager.instance.whichScreen.equals("GameplayScreen")) {
//            if (!gameData.isGoodPhone) {
//                Assets.getInstance().unloadGameplayAssets();
//            }
//        }
//    }
//
//    //设置引导
//    Image yindaoShou;
//    Image[][] yindaoGeziImage;
//    boolean hasSetYindao = false;
//
//    void setYindao() {
//        if (hasSetYindao) {
//            return;
//        }
//
//        hasSetYindao = true;
//        yindaoShou = new Image(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/shou"));
//        yindaoShou.setTouchable(Touchable.disabled);
//        stage.addActor(yindaoShou);
//        yindaoShou.setVisible(false);
//
//        yindaoMask = new Image(new NinePatch(Assets.getInstance().assetManager.get(Constants.gongyongAtlasPath, PlistAtlas.class).findRegion("gongyong/atlas/mask"), 1, 1, 1, 1));
//        yindaoMask.setName("yindaoMask");
//        yindaoMask.setColor(yindaoMask.getColor().r, yindaoMask.getColor().g, yindaoMask.getColor().b, 0.65f);
//        yindaoMask.setSize(ViewportUtils.getWidth(), ViewportUtils.getHeight());
//        yindaoMask.setPosition(ViewportUtils.getLeft(), ViewportUtils.getBottom());
//        stage.addActor(yindaoMask);
//        yindaoMask.setVisible(false);
//
//        //为正常模式
//        if (gameMode == MyEnum.GameMode.normalMode) {
//            if (gameData.gameIs == 0 && !gameData.isYindaoed[0]) {
//                isYindaoWithoutState = true;
//                isWordYindao = true;
//                //当是第一关且未被引导的时候
//                yindaoMask.setVisible(true);
//                yindaoMask.toFront();
//
//                yindaoGeziImage = new Image[h][w];
//                for (int i = 0; i < h; i++) {
//                    for (int j = 0; j < w; j++) {
//                        if (geziGroup[i][j] != null) {
//                            yindaoGeziImage[i][j] = new Image(fingerImagePatch);
//                            yindaoGeziImage[i][j].setSize(geziSizeX + 4, geziSizeY + 4);
//                            yindaoGeziImage[i][j].setPosition(geziPos[i][j].x - 2, geziPos[i][j].y - 2);
//                            stage.addActor(yindaoGeziImage[i][j]);
//                            yindaoGeziImage[i][j].setVisible(false);
//                        }
//                    }
//                }
//
//                setNormalPinToFront();
//
//                int index = 0;
//                String useWord = wordMsg[index].str;
//
//                yindaoWordIndex = index;
//
//                zimuShunxu = new int[useWord.length()];
//                //判断拼装框下面的字母是否已经被使用过了
//                boolean[] use = new boolean[letterLength];
//                for (int i = 0; i < letterLength; i++) {
//                    use[i] = false;
//                }
//                for (int i = 0; i < zimuShunxu.length; i++) {
//                    for (int j = 0; j < letterLength; j++) {
//                        //当字母相同的时候并且这个字母没有被使用的时候
//                        if (useWord.charAt(i) == gameLetter.charAt(j) && !use[j]) {
//                            use[j] = true;
//                            zimuShunxu[i] = j;
//                            break;
//                        }
//                    }
//                }
//
//                setYindaoActions();
//                yindaoPanel.updateYindao(0, 0, yindaoPanel.getStartPosY() + 20 - ViewportUtils.getDeltaY(), useWord, true);
//                yindaoPanel.toFront();
//                yindaoPanel.show();
//                setWordZindex(index, Constants.MAX_INTEGER);
//
//            } else if ((gameData.gameIs == Constants.hintYindaoStart && !gameData.isYindaoed[1])) {
//                //当为第3关且未被引导的时候
//                yindaoPanel.updateYindao(2, 0, yindaoPanel.getStartPosY(),
//                        new float[]{hintBtnPos.x - 28.5f, hintBtn.getY() + hintBtn.getHeight() / 2 - 30, 90,
//                                shuffleBtnPos.x - 28.5f, shuffleBtn.getY() + shuffleBtn.getHeight() / 2 - 30, 90}, true);
//                showPanel(yindaoPanel);
//
//                hintBtn.toFront();
//                shuffleBtn.toFront();
//
//            } else if (gameData.gameIs == Constants.fingerStart && !gameData.isYindaoed[2]) {
//                //当为第21关且未被引导的时候
//                Prefs.putBoolean("isFingerFree", true);
//                yindaoPanel.updateYindao(20, 0, fingerBtn.getTop() + 80,
//                        new float[]{fingerBtn.getX() + fingerBtn.getWidth() / 2, fingerBtn.getTop(), 0}, true);
//                showPanel(yindaoPanel);
//                fingerBtn.toFront();
//                try {
//                    fingerBtn.setCanVisible(true);
//                    fingerBtn.setVisible(true);
//                    fingerBtn.setPos(fingerBtnPos);
//                }catch (Exception e){
//
//                }
//
//            } else if (gameData.gameIs == 10 && !gameData.isYindaoed[7]) {
//                gameData.setYindao(7);
//                //当为第10关且未被引导的时候，展示金币单词的引导
//                int index = -1;
//                for (int j = 0; j < wordCount; j++) {
//                    if (coinWord.equals(wordMsg[j].str)) {
//                        index = j;
//                        break;
//                    }
//                }
//                //todo：这里下次需要注意
//                int[] pos = wordToGrid(index);
//                //水平
//                //金币单词中间格子的位置
//                //竖直的时候为geziGroup[pos[pos.length - 2]][pos[pos.length - 1]]
////				float tx = geziGroup[pos[pos.length/2 - 1]][pos[pos.length/2]].getX();
////				float ty =geziGroup[pos[pos.length/2 - 2]][pos[pos.length/2]].getY();
//
//                if (index >= 0) {
//                    if (wordMsg[index].dir == MyEnum.Dir.vertical) {
//                        if (pos != null && pos[pos.length - 2] < h && pos[pos.length - 1] < w && geziGroup[pos[pos.length - 2]][pos[pos.length - 1]] != null) {
//                            //避免如果格子真为空时候的bug
//                            float tx = geziGroup[pos[pos.length - 2]][pos[pos.length - 1]].getX();
//                            float ty = geziGroup[pos[pos.length - 2]][pos[pos.length - 1]].getY();
//                            yindaoPanel.updateYindao(9, 0, ty - 335,
//                                    new float[]{tx + halfGeziSizeX, ty + 5, 180}, true);
//                            showPanel(yindaoPanel);
//                            for (int i = 0; i < pos.length / 2; i++) {
//                                geziGroup[pos[2 * i]][pos[2 * i + 1]].toFront();
//                                letter[pos[2 * i]][pos[2 * i + 1]].toFront();
//                                if (coinGeziImages[pos[2 * i]][pos[2 * i + 1]] != null) {
//                                    coinGeziImages[pos[2 * i]][pos[2 * i + 1]].toFront();
//                                }
//                            }
//                        }
//                    } else {
//                        if (pos != null && pos[pos.length / 2 - 1] < h && pos[pos.length / 2] < w && geziGroup[pos[pos.length / 2 - 1]][pos[pos.length / 2]] != null) {
//                            //避免如果格子真为空时候的bug
//                            float tx = geziGroup[pos[pos.length / 2 - 1]][pos[pos.length / 2]].getX();
//                            float ty = geziGroup[pos[pos.length / 2 - 1]][pos[pos.length / 2]].getY();
//                            yindaoPanel.updateYindao(9, 0, ty - 335,
//                                    new float[]{tx + halfGeziSizeX, ty + 5, 180}, true);
//                            showPanel(yindaoPanel);
//                            for (int i = 0; i < pos.length / 2; i++) {
//                                geziGroup[pos[2 * i]][pos[2 * i + 1]].toFront();
//                                letter[pos[2 * i]][pos[2 * i + 1]].toFront();
//                                if (coinGeziImages[pos[2 * i]][pos[2 * i + 1]] != null) {
//                                    coinGeziImages[pos[2 * i]][pos[2 * i + 1]].toFront();
//                                }
//                            }
//                        }
//                    }
//                }
//
//
//            } else if (gameData.gameIs == Constants.fasthintStart && !gameData.isYindaoed[15]) {
//                yindaoPanel.updateYindao(49, 0, fasthintBtn.getTop() + 80,
//                        new float[]{fasthintBtn.getX() + fasthintBtn.getWidth() / 2, fasthintBtn.getTop(), 0}, true);
//                showPanel(yindaoPanel);
//                fasthintBtn.toFront();
//
//            } else if (gameData.gameIs == Constants.showNovecesRewardPanelGameIs && !Prefs.getBoolean("hasShowNovicesRewardPanel", false)) {
//                showPanel(novicesRewardPanel);
//            } else if(beePositions!=null && !gameData.isYindaoed[21]){
//                yindaoPanel.updateYindao(101, 0, yindaoPanel.getStartPosY() + 65 - ViewportUtils.getDeltaY() / 2, "", true);
//                showPanel(yindaoPanel);
//            }
//            //test
////            packShopPanel.initPanel();
////            showPanel(packShopPanel);
//
//        } else if (gameMode == MyEnum.GameMode.dailyMode) {
//            //每日挑战
//            if (!gameData.isYindaoed[14]) {
//                isWordYindao = true;
//                yindaoMask.setVisible(true);
//                yindaoMask.toFront();
//
//                setNormalPinToFront();
//
////				setPinToFront();
//
//                String useWord = wordMsg[fengPosition.index].str;
//
//                int index = fengPosition.index;
//                yindaoWordIndex = index;
//
//                zimuShunxu = new int[useWord.length()];
//                //判断拼装框下面的字母是否已经被使用过了
//                boolean use[] = new boolean[letterLength];
//                for (int i = 0; i < letterLength; i++) {
//                    use[i] = false;
//                }
//                for (int i = 0; i < zimuShunxu.length; i++) {
//                    for (int j = 0; j < letterLength; j++) {
//                        //当字母相同的时候并且这个字母没有被使用的时候
//                        if (useWord.charAt(i) == gameLetter.charAt(j) && !use[j]) {
//                            use[j] = true;
//                            zimuShunxu[i] = j;
//                            break;
//                        }
//                    }
//                }
//
//                setYindaoActions();
//
//
//                yindaoPanel.updateYindao(24, 2, yindaoPanel.getStartPosY() + 65 - ViewportUtils.getDeltaY() / 2, useWord, true);
//                yindaoPanel.toFront();
//                yindaoPanel.show();
//
//                setWordZindex(index, Constants.MAX_INTEGER);
//                if (warningGroup.isShowing) {
//                    feng.setZIndex(warningGroup.getZIndex() - 1);
//                } else if (questWarngingGroup.isShowing) {
//                    feng.setZIndex(questWarngingGroup.getZIndex() - 1);
//                } else {
//                    feng.toFront();
//                }
//            }
//        }
//    }
//
//
//    //隐藏引导的东西
//    public void hideYindao() {
//        canLianxian = true;
//        yindaoMask.setVisible(false);
//        yindaoShou.setVisible(false);
//        for (int i = 0; i < yindaoXuxian.length; i++) {
//            yindaoXuxian[i].setVisible(false);
//        }
//
//        //阴影盖住按钮，把这个拼装框的设置成第一个添加的按钮的zindex后面
////		pin.setZIndex(fasthintBtn.getZIndex() - 1);
//        pinGroup.setZIndex(fasthintBtn.getZIndex());
//
//        if (useKuaiB) {
//            setPinToFront();
//        } else {
//            setNormalPinToFront();
//        }
//        yindaoPanel.toFront();
//
//    }
//
//    int yindaoXuxianCnt = 0;
//
//    void setYindaoActions() {
//
//        //给引导手和虚线初始化
//
//
//        //设置一下字母组
////		lettersGroup.toFront();
//
//        pinGroup.toFront();
//        for (int i = 0; i < zimuShunxu.length - 1; i++) {
//            yindaoXuxian[i].setPoint(getOriginXByRank(zimuShunxu[i + 1], letterLength) + pinGroupPos.x, getOriginYByRank(zimuShunxu[i + 1], letterLength) + pinGroupPos.y,
//                    getOriginXByRank(zimuShunxu[i], letterLength) + pinGroupPos.x, getOriginYByRank(zimuShunxu[i], letterLength) + pinGroupPos.y);
//            yindaoXuxian[i].setVisible(true);
//            yindaoXuxian[i].toFront();
//        }
//        for (int i = zimuShunxu.length - 1; i < 6; i++) {
//            yindaoXuxian[i].setVisible(false);
//        }
//
//        yindaoXuxianCnt = zimuShunxu.length - 1;
//
//        //设置手的运动轨迹，先往第2个点，第3个点等前进，当到最后一个点时，将其位置设置为第一个点，起始手的位置设置为第一个点
//        SequenceAction sequenceAction = new SequenceAction();
//        for (int i = 1; i < zimuShunxu.length; i++) {
//            sequenceAction.addAction(Actions.moveTo(getYindaoShouXByRank(zimuShunxu[i], letterLength) + pinGroupPos.x,
//                    getYindaoShouYByRank(zimuShunxu[i], letterLength) + pinGroupPos.y, 1.2f));
//            sequenceAction.addAction(Actions.delay(0.4f));
//        }
//        sequenceAction.addAction(Actions.moveTo(getYindaoShouXByRank(zimuShunxu[0], letterLength) + pinGroupPos.x,
//                getYindaoShouYByRank(zimuShunxu[0], letterLength) + pinGroupPos.y));
//        sequenceAction.addAction(Actions.delay(0.4f));
//
//        yindaoShou.clearActions();
//        yindaoShou.toFront();
//        yindaoShou.setVisible(true);
//        yindaoShou.addAction(Actions.forever(sequenceAction));
//        yindaoShou.setPosition(getYindaoShouXByRank(zimuShunxu[0], letterLength) + pinGroupPos.x, getYindaoShouYByRank(zimuShunxu[0], letterLength) + pinGroupPos.y);
//    }
//
//
//    public void setPinToFront() {
//        pin.setColor(1, 1, 1, 0.7f);
//        useWhiterPin = true;
//        pinGroup.toFront();
////		lettersGroup.toFront();
//        for (int i = 0; i < letterLength; i++) {
//
//            bottomLetterLbl[i].setColor(0, 0, 0, 1);
//        }
//        lettersGroup.toFront();
//    }
//
//    public void setNormalPinToFront() {
//        pin.setColor(0, 0, 0, 0.5f);
//        useWhiterPin = false;
//        pinGroup.toFront();
////		lettersGroup.toFront();
//        for (int i = 0; i < letterLength; i++) {
//            bottomLetterLbl[i].setColor(1f, 1f, 1f, 1);
//        }
//        lettersGroup.toFront();
//    }
//
//    //隐藏格子后去其他场景
//    void gameplayGotoScreen(Runnable runnable) {
//        float delayTime = setHideAction(true);
//        runDelay(runnable, delayTime);
//
//    }
//
//
//    public void update(){
//        super.update();
//        if (hintBtn != null) {
//            hintBtn.setNum(gameData.hintNum);
//        }
//        if (fingerBtn != null) {
//            if(!fingerBtn.isFree()){
//                fingerBtn.setNum(gameData.fingerNum);
//            }
////			if (gameData.fingerNum != 0){
////				fingerBtn.setCanVisible(true);
////			}
//        }
//        if (fasthintBtn != null) {
//            fasthintBtn.setNum(gameData.fasthintNum);
//
//        }
//
//        if(spellingBeeBtn!=null && SpellingBeeManager.getInstance().getState() == SpellingBeeManager.State.NONE){
//            spellingBeeBtn.clearActions();
//            spellingBeeBtn.addAction(Actions.sequence(Actions.scaleTo(1.05F, 1.05F,0.1F)
//                    , Actions.scaleTo(0, 0, 0.2f), Actions.removeActor()));
//            spellingBeeBtn = null;
//        }
//    }
//
//    //当背景图更换时更改背景
//    public void switchBg() {
//        if (useKuaiB != gameData.useKuaiB) {
//            useKuaiB = gameData.useKuaiB;
//            skinName = gameData.skinName;
//            if (skinName.equals("skin14")) {
//                isSkinAlwaysBlack = true;
//            } else if (gameData.nowBgName.equals("120")) {
//                isSkinAlwaysBlack = true;
//            } else {
//                isSkinAlwaysBlack = false;
//            }
//            if (useKuaiB || !gameData.isYindaoed[0]) {
//                videoBox.setSkin("baise");
//                pin.setColor(1, 1, 1, 0.7f);
//                useWhiterPin = true;
//                for (int i = 0; i < letterLength; i++) {
//                    bottomLetterLbl[i].setColor(0f, 0f, 0f, 1);
//                }
//
//            } else {
//                videoBox.setSkin("heise");
//                pin.setColor(0, 0, 0, 0.5f);
//                useWhiterPin = false;
//                for (int i = 0; i < letterLength; i++) {
//                    bottomLetterLbl[i].setColor(1f, 1f, 1f, 1);
//                }
//            }
//
//
//            if (useKuaiB) {
//                baikuaiPatch = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/atlas/kuaib")));
//                for (int i = 0; i < h; i++) {
//                    for (int j = 0; j < w; j++) {
//                        if (zmcxSpineGroup[i][j] != null) {
//                            zmcxSpineGroup[i][j] = zmcxBSpineGroup[i][j];
//                        }
//                    }
//                }
//            } else {
//                baikuaiPatch = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/atlas/kuai")));
//                for (int i = 0; i < h; i++) {
//                    for (int j = 0; j < w; j++) {
//                        if (zmcxSpineGroup[i][j] != null) {
//                            zmcxSpineGroup[i][j] = zmcxASpineGroup[i][j];
//                        }
//                    }
//                }
//            }
//
//            int index = Integer.parseInt(skinName.replace("skin", ""));
//            skin = new TextureRegionDrawable(new TextureRegion(Assets.getInstance().assetManager.get("ui/gameplayNew/gameplayNewPlist.plist", PlistAtlas.class).findRegion("gameplayNew/skin/skin" + index)));
//            skinColor = Constants.SkinColor[index];
//            for (int i = 0; i < h; i++) {
//                for (int j = 0; j < w; j++) {
//                    if (geziImages[i][j] != null) {
//                        geziImages[i][j].setDrawable(baikuaiPatch);
//                        fingerImages[i][j].setDrawable(fingerImagePatch);
//                        skinImages[i][j].setDrawable(skin);
//                        if (useKuaiB) {
//                            if (gridMsg[i][j].appear != MyEnum.WordAppear.appear) {
//                                letterLbl[i][j].setColor(Color.BLACK);
//                            }
//                        } else {
//                            letterLbl[i][j].setColor(Color.WHITE);
//                        }
//
//                    }
//                }
//            }
//
//            for (int i = 0; i < letterLength; i++) {
//                woodImage[i].setColor(skinColor);
//            }
//
//            preview.setSkinColor(skinColor);
//            for (int i = 0; i < extraWordGroupMaxn; i++) {
//                extraWordGroup[i].setSkinColor(skinColor);
//            }
//
//
//            myLine.setColor(skinColor);
//        }
//    }
//
//
//    public GamePlayButton setGameplayButtonCanVisible(String name) {
//        if (name.equals("fasthint")) {
//            return fasthintBtn;
//        } else if (name.equals("finger")) {
//            return fingerBtn;
//        } else {
//            return hintBtn;
//        }
//    }
//
//    //道具弹窗展示间隔1分钟只展示一次，只考虑render
//    final float PROPREWARDPANELSHOW_TIME_GAP = 60;
//    //道具弹窗展示间隔
//    float propRewardPanelShowTime = 61;
//    //是否需要展示道具弹窗了，在mayfeelhard的时候设置为true
//    boolean needToShowDNAPropRewardPanel = false;
//
//    public void showDNAPropRewardPanel() {
//        needToShowDNAPropRewardPanel = false;
//
//        boolean canShowPanel = true;
//        if (gameData.propPanelUserType.equals("AUser")) {
//            if (gameData.propNotShowLevelTime < 10) {
////        		return;
//                canShowPanel = false;
//            }
//        } else if (gameData.propPanelUserType.equals("BUser")) {
//            if (gameData.usePropPanelHint) {
//                if (gameData.propNotShowLevelTime < 2) {
//                    canShowPanel = false;
////        			return;
//                }
//            } else if (gameData.propNotShowLevelTime < 3) {
//                canShowPanel = false;
////        		return;
//            }
//        }
//
//        if (PlatformManager.instance.isRewaedVideoReady() && propRewardPanelShowTime > PROPREWARDPANELSHOW_TIME_GAP) {
//            if (gameData.dnaSetPropPanelLessFlag) {
//                if (gameData.coinNumber < gameData.dnaSetPropLessCoin && canShowPanel) {
//                    propRewardPanelShowTime = 0f;
//                    showPanel(propRewardPanel);
//                    return;
//                }
//            }
//
//
//            if (gameData.dnaSetPropPanelMoreFlag) {
//                gameData.dnaSetPropPanelNowM++;
//                if (gameData.coinNumber > gameData.dnaSetPropPanelN && gameData.dnaSetPropPanelNowM >= gameData.dnaSetPropPanelM) {
//
//                    if (canShowPanel) {
//                        propRewardPanelShowTime = 0f;
//                        gameData.dnaSetPropPanelNowM = 0;
//                        showPanel(propRewardPanel);
//                    }
//
//                    return;
//                }
//
//                Prefs.putInteger("dnaSetPropPanelNowM", gameData.dnaSetPropPanelNowM);
//            }
//        }
//
//    }
//
//    //卸载不必要的资源
//    public void unloadDoNotNeedBg() {
//		/*
//		如设置Prefs_GameSolved为2439，此时进入游戏，因为需要下载的图没下载好，会使用初始夏天的图和上面那个9的图，这个时候在换
//		壁纸页面下载这一章的图，进入游戏，会同时在资源中有夏天的图，9的图和本章下载的图
//		此时其实应该卸载9的图，就是卸载非nowBgName和非下一章的图
//		 */
//
//        for (int i = 0; i < game.bgAllNum; i++) {
//            if (game.bgName[i] != null && (!game.bgName[i].equals(gameData.nowBgName) && !game.bgName[i].equals(gameData.nextChapterBgName))) {
//                game.disposeBg(i);
//            }
//        }
//    }
//
//    public DailyProgress getDailyProgress() {
//        return dailyProgress;
//    }
//}
//
//
