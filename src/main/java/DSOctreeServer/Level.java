package DSOctreeServer;

public enum Level{
    Level1(8);

    private Level(int value){
        this.level = value;
    }

    private int level;

    public int getValue(){
        return this.level;
    }

    public void setValue(int value){
        if(value == 8)
            this.level = 256;
        if(value == 16)
            this.level = 128;
        if(value == 32)
            this.level = 64;
        if(value == 64)
            this.level = 32;
        if(value == 128)
            this.level = 16;
        if(value == 256)
            this.level = 8;
    }

}
