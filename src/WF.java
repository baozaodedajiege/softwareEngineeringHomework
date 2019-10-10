import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WF {
    /**
     * -c <file>  命令：java WF -c 词频测试数据集/1.IHaveaDream.txt
     * -f <file> (-n 10) 命令：java WF -f 词频测试数据集/1.IHaveaDream.txt -n 10
     * -d <directory> (-n 10) 命令：java WF -d 词频测试数据集/ -n 10
     * -d -s  <directory> (-n 10) 命令：java WF -d -s 词频测试数据集/ -n 10
     * -p <number> <file> 命令：java WF -p 3 词频测试数据集/1.IHaveaDream.txt
     * -v <file> <verb file> (-n 10) 命令：java WF -v 词频测试数据集/test.txt 词频测试数据集/verb.txt -n 10
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        WF wf = new WF();
        if(args[0].equals("-c")){
            wf.step0(args[1]);
        }
        else if (args[0].equals("-f")){
            String n = "0";
            if( Arrays.asList(args).contains("-n")){
                n = args[3];
            }
            wf.step1_01(args[1],Integer.valueOf(n).intValue());
        }
        else if (args[0].equals("-d")){
            String n = "0";
            if( Arrays.asList(args).contains("-s")){
                if( Arrays.asList(args).contains("-n")){
                    n = args[4];
                }
                wf.step1_03(args[2],Integer.valueOf(n).intValue());
            }
            else {
                if( Arrays.asList(args).contains("-n")){
                    n = args[3];
                }
                wf.step1_02(args[1],Integer.valueOf(n).intValue());
            }
        }
        else if (args[0].equals("-p")){
            String num = args[1] , file = args[2];
            wf.step3(file,Integer.valueOf(num).intValue());
        }
        else if (args[0].equals("-v")){
            String file = args[1] , verbFile = args[2] , n = "0";
            if(Arrays.asList(args).contains("-n")){
                n = args[4];
            }
            wf.step4(file,verbFile,Integer.valueOf(n).intValue());
        }
        else {
            System.out.println("指令错误！");
        }
    }

    /**
     * 第0步
     * @param filename
     * @throws IOException
     */
    public void step0(String filename) throws IOException {
        Map<Character,Integer> map = new HashMap<>();
        double num = 0;
        FileReader reader = new FileReader(filename);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            for(int i = 0 ; i < line.length() ; i++){
                char s = line.charAt(i);
                if(isZm(s)){
                    num++;
                    if(map.containsKey(s)){
                        map.put(s,map.get(s)+1);
                    }else{
                        map.put(s,1);
                    }
                }
            }
        }
        List<Map.Entry<Character,Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                if (o1.getValue() != o2.getValue())
                    return o2.getValue() - o1.getValue();
                else
                    return o1.getKey() - o2.getKey();
            }
        });
        for (Map.Entry s : list) {
            System.out.print(s.getKey()+"--");
            double pl = (int)s.getValue()/num;
            System.out.println(String.format("%.6f", pl));
        }
    }

    /**
     * 判断是否为字母
     * @param s
     * @return
     */
    public boolean isZm(char s){
        if(s >= 'a' && s <= 'z')    return true;
        if(s >= 'A' && s <= 'Z')    return true;
        return false;
    }

    /**
     * 读文件
     * 返回类型为字符串
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readfile(String fileName) throws IOException {
        File file = new File(fileName);//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
        }
        bReader.close();
        String text = sb.toString();
        return text;
    }

    /**
     * 第一步的功能1
     * @param fileName
     * @param n
     * @throws IOException
     */
    public void step1_01(String fileName,int n) throws IOException {
        String text = readfile(fileName);
        text = text.replaceAll("[^0-9a-zA-Z]+", " ");
        String words[] = text.split("\\s+");//分割一个或多个空格
        Map< String , Integer > count = new TreeMap< String , Integer >();
        for( String word : words ){
            int cnt = 1;
            if( count.get(word) != null ) cnt = count.get(word) + 1;
            count.put( word , cnt );
        }
        List<Map.Entry<String, Integer>> printWords = new ArrayList<Map.Entry<String, Integer>>(count.entrySet());
        Collections.sort(printWords, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(o1.getValue().compareTo(o2.getValue()) == 0){
                    return o1.getKey().compareTo(o2.getKey());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int t = 0;
        for( Map.Entry< String , Integer > word : printWords){
            System.out.println( word.getKey()+":" + word.getValue() );
            t = t+1;
            if(n!=0&&t>=n)    break;
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1)
            return fileName.substring(fileName.lastIndexOf("."));
        else {
            return null;
        }
    }

    /**
     * 第一步功能2
     * @param directory
     * @param n
     * @throws IOException
     */
    public void step1_02(String directory,int n) throws IOException {
        File file = new File(directory);//获取其file对象
        File[] files = file.listFiles();//遍历path下的文件和目录，放在File数组中
        for (File f:files){
            if(!f.isDirectory() && getFileExtension(f).equals(".txt") ){
                String text = readfile(directory + f.getName());
                System.out.println(f.getName() + "的单词统计功能如下:");
                step1_01(f.toString(),n);
            }
        }
    }

    /**
     * 递归查找文件
     * @param file
     */
    public static Vector<String>needFilesPath = new Vector<String>();
    private static void findAllFiles(File file , String filePath , Vector<String>needFilesPath){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory()){
                findAllFiles( f , filePath + "\\" + f.getName() + "\\" , needFilesPath );//若是目录，则递归打印该目录下的文件
            }
            if(f.isFile()){
                needFilesPath.add(filePath + f.getName());//若是文件，直接保存路径
            }
        }
    }

    /**
     * 第一步功能3
     * @param directoryPath
     * @param n
     * @throws IOException
     */
    public void step1_03(String directoryPath,int n) throws IOException {
        File file = new File(directoryPath);//获取其file对象
        String filePath = directoryPath;
        findAllFiles(file , filePath , needFilesPath);
        for( String filesPath : needFilesPath ){
            System.out.println(filesPath + "的单词统计功能如下:");
            step1_01(filesPath,n);
        }
        needFilesPath.clear();//清空里面的数据
    }

    /**
     * 第三步
     * @param fileName
     * @param n
     * @throws IOException
     */
    public void step3(String fileName,int n) throws IOException {
        String text = readfile(fileName);
        text = text.replaceAll("[^0-9a-zA-Z\\s+]+", ",");
        String texts[] = text.split(",");
        List<String> list = new ArrayList<>();
        for (String s : texts){
            String[] ss = s.trim().split("\\s+");
            if(ss.length == n){
                list.add(s.trim());
            }
        }
        Collections.sort(list);
        for (String l:list) {
            System.out.println(l);
        }
    }

    /**
     * 第四步
     * @param fileName
     * @param verbFileName
     * @param n
     * @throws IOException
     */
    public void step4(String fileName,String verbFileName,int n) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(verbFileName));
        String line;
        Vector< Vector<String> >verb = new Vector< Vector<String> >();
        while ( ( line = in.readLine() ) != null ) {
            String words[] = line.split("\\s+");//分割一个或多个空格
            Vector<String>one = new Vector<String>();
            for( String string : words ) one.add(string);
            verb.add(one);
        }
        String text = readfile(fileName);
        text = text.replaceAll("[^0-9a-zA-Z]+", " ");
        String words[] = text.split("\\s+");//分割一个或多个空格
        Map< String , Integer > count = new TreeMap< String , Integer >();
        for( String word : words ){
            int cnt = 1;
            if( count.get(word) != null ) cnt = count.get(word) + 1;
            count.put( word , cnt );
        }
        for( int iii = 0 ; iii < verb.size() ; iii++ ){
            int sum = 0;
            for( String word:verb.get(iii) ){
                if( count.get(word) != null ){
                    sum += count.get(word);
                    count.remove(word);
                }
            }
            count.put(verb.get(iii).get(0), sum);
        }
        List<Map.Entry<String, Integer>> printWords = new ArrayList<Map.Entry<String, Integer>>(count.entrySet());
        Collections.sort(printWords, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(o1.getValue().compareTo(o2.getValue()) == 0){
                    return o1.getKey().compareTo(o2.getKey());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int t = 0;
        for( Map.Entry< String , Integer > word : printWords){
            System.out.println( word.getKey()+":" + word.getValue() );
            t = t+1;
            if(n!=0&&t>=n)    break;
        }
    }
}