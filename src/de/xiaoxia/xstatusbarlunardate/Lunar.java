package de.xiaoxia.xstatusbarlunardate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ṩһЩũ�������Ϣ
 * @mod xiaoxia.de
 * @author joyphper.net
 */
public class Lunar {

    
    private Calendar solar;
    private int lunarYear;
    private int lunarMonth;
    private int lunarDay;
    private boolean isLeap;
    private boolean isLeapYear;
    private int solarYear;
    private int solarMonth;
    private int solarDay;
    private int cyclicalYear = 0;
    private int cyclicalMonth = 0;
    private int cyclicalDay = 0;
    
    private String lFestivalName;
    private String termString;
    private String clFestivalName;
    private String sFestivalName;
    private String csFestivalName;

    private final static int[] lunarInfo = {
        0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af,
        0x9ad0, 0x55d2, 0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd295, 0xb54f,
        0xd6a0, 0xada2, 0x95b0, 0x4977, 0x497f, 0xa4b0, 0xb4b5, 0x6a50,
        0x6d40, 0xab54, 0x2b6f, 0x9570, 0x52f2, 0x4970, 0x6566, 0xd4a0,
        0xea50, 0x6a95, 0x5adf, 0x2b60, 0x86e3, 0x92ef, 0xc8d7, 0xc95f,
        0xd4a0, 0xd8a6, 0xb55f, 0x56a0, 0xa5b4, 0x25df, 0x92d0, 0xd2b2,
        0xa950, 0xb557, 0x6ca0, 0xb550, 0x5355, 0x4daf, 0xa5b0, 0x4573,
        0x52bf, 0xa9a8, 0xe950, 0x6aa0, 0xaea6, 0xab50, 0x4b60, 0xaae4,
        0xa570, 0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0, 0x96d0, 0x4dd5,
        0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6,
        0x95bf, 0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46,
        0xab60, 0x9570, 0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58,
        0x5ac0, 0xab60, 0x96d5, 0x92e0, 0xc960, 0xd954, 0xd4a0, 0xda50,
        0x7552, 0x56a0, 0xabb7, 0x25d0, 0x92d0, 0xcab5, 0xa950, 0xb4a0,
        0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0, 0x5176, 0x52bf, 0xa930,
        0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6, 0xa4e0, 0xd260,
        0xea65, 0xd530, 0x5aa0, 0x76a3, 0x96d0, 0x4afb, 0x4ad0, 0xa4d0,
        0xd0b6, 0xd25f, 0xd520, 0xdd45, 0xb5a0, 0x56d0, 0x55b2, 0x49b0,
        0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0, 0x4b63, 0x937f,
        0x49f8, 0x4970, 0x64b0, 0x68a6, 0xea5f, 0x6b20, 0xa6c4, 0xaaef,
        0x92e0, 0xd2e3, 0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0,
        0xa6d0, 0x55d4, 0x52d0, 0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50,
        0x55a0, 0xaba4, 0xa5b0, 0x52b0, 0xb273, 0x6930, 0x7337, 0x6aa0,
        0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4, 0xd260, 0xe968, 0xd520,
        0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0, 0xd150, 0xf252, 0xd520
    };
    private final static int[] solarTermInfo = {
             0,  21208,  42467,  63836,  85337, 107014, 128867, 150921,
        173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033,
        353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758
    };
    private final static String[] Tianan = {
        "��", "��", "��", "��", "��", "��", "��", "��", "��", "��"
    };
    private final static String[] lunarString1 = {
        "��", "һ", "��", "��", "��", "��", "��", "��", "��", "��"
    };
    private static String[] lunarString2;
    private static String[] Deqi;
    private static String[] Animals;
    private static String[] solarTerm;
    private static String[] lFtv;
    private static String[] sFtv;
    private static String[] wFtv;

    private final static Pattern sFreg = Pattern.compile("^(\\d{2})(\\d{2})(.+)$");
    private final static Pattern wFreg = Pattern.compile("^(\\d{2})(\\d)(\\d)(.+)$");

    private static GregorianCalendar utcCal = null;
    
    private static int toInt(String str) {
        try { return Integer.parseInt(str); }
        catch(Exception e) { return -1; }
    }
    private synchronized void findFestival() {
		int sM = this.getSolarMonth();
		int sD = this.getSolarDay();
		int sY = this.getSolarYear();
		int lM = this.getLunarMonth();
		int lD = this.getLunarDay();
		Matcher m;
		
		//�������ռ���
		if(Main._solar){
			for (int i=0; i<Lunar.sFtv.length; i++) {
				m = Lunar.sFreg.matcher(Lunar.sFtv[i]);
				if (m.find()) {
					if (sM == Lunar.toInt(m.group(1)) && sD == Lunar.toInt(m.group(2))) {
						this.sFestivalName = m.group(3);
						break;
					}
				}
			}
			//���㸴���
			if(Main._lang == 4 || Main._lang == 5){
				int a = sY % 19;
				int b = (int) Math.floor(sY / 100);
				int c = sY % 100;
				int d = (int) Math.floor(b / 4);
				int e = b % 4;
				int f = (int) Math.floor((b + 8) / 25);
				int g = (int) Math.floor((b - f + 1) / 3);
				int h = (19 * a + b - d - g + 15) % 30;
				int i = (int) Math.floor(c / 4);
				int k = c % 4;
				int l = (32 + 2 * e + 2 * i - h - k) % 7;
				int z = (int) Math.floor((a + 11 * h + 22 * l) / 451);
				if(sM == (int) Math.floor((h + l - 7 * z + 114) / 31) && sD == ((h + l - 7 * z + 114) % 31) + 1){
					this.sFestivalName += " " + "�ͻ" ;
					this.sFestivalName = this.sFestivalName.replaceFirst("^\\s", "");
				}
			}
		}
		
		//ũ��
		if(Main._fest){
	        for (int i=0; i<Lunar.lFtv.length; i++) {
	            m = Lunar.sFreg.matcher(Lunar.lFtv[i]);
	            if (m.find()) {
	                if (lM == Lunar.toInt(m.group(1)) && lD == Lunar.toInt(m.group(2))) {
	                    this.lFestivalName = m.group(3);
	                    break;
	                }
	    			if(lM == 12 && lD == 29 && Lunar.getLunarMonthDays(this.lunarYear, lM) == 29){
	    				this.lFestivalName += "��Ϧ";
	    				break;
	    			}
	            }
	        }
		}
		
		//�Զ���ũ��
		if(Main._custom){
	        for (int i=0; i<Main._clf.length; i++) {
	            m = Lunar.sFreg.matcher(Main._clf[i]);
	            if (m.find()) {
	                if (lM == Lunar.toInt(m.group(1)) && lD == Lunar.toInt(m.group(2))) {
	                    this.clFestivalName = m.group(3);
	                    break;
	                }
	            }
	        }
		}
		
		//�Զ��幫��
		if(Main._solar_custom){
	        for (int i=0; i<Main._csf.length; i++) {
	            m = Lunar.sFreg.matcher(Main._csf[i]);
				if (m.find()) {
					if (sM == Lunar.toInt(m.group(1)) && sD == Lunar.toInt(m.group(2))) {
						this.csFestivalName = m.group(3);
						break;
					}
				}
	        }
		}
		
		// ���ܽ���
		if(Main._solar){
			int w, d;
			for (int i=0; i<Lunar.wFtv.length; i++) {
				m = Lunar.wFreg.matcher(Lunar.wFtv[i]);
				if (m.find()) {
					if (this.getSolarMonth() == Lunar.toInt(m.group(1))) {
						w = Lunar.toInt(m.group(2));
						d = Lunar.toInt(m.group(3));
						if (this.solar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == w &&
								this.solar.get(Calendar.DAY_OF_WEEK) == d) {
							this.sFestivalName += " " + m.group(4);
						}
					}
				}
			}
			this.sFestivalName = this.sFestivalName.replaceFirst("^\\s", "");
		}
    }
    
    /**
     * ����ũ���������·�
     * @param lunarYear
     *            ָ��ũ�����(����)
     * @return ��ũ�������µ��·�(����,û�򷵻�0)
     */
    private static int getLunarLeapMonth(int lunarYear) {
        // ���ݱ���,ÿ��ũ������16bit����ʾ,
        // ǰ12bit�ֱ��ʾ12���·ݵĴ�С��,���4bit��ʾ����
        // ��4bitȫΪ1��ȫΪ0,��ʾû��, ����4bit��ֵΪ�����·�
        int leapMonth = Lunar.lunarInfo[lunarYear - 1900] & 0xf;
        leapMonth = (leapMonth == 0xf ? 0 : leapMonth);
        return leapMonth;
    }

    /**
     * ����ũ�������µ�����
     * 
     * @param lunarYear ָ��ũ�����(����)
     * @return ��ũ�������µ�����(����)
     */
    private static int getLunarLeapDays(int lunarYear) {
        // ��һ�����4bitΪ1111,����30(����)
        // ��һ�����4bit��Ϊ1111,����29(С��)
        // ������û������,����0
        return Lunar.getLunarLeapMonth(lunarYear) > 0 ? ((Lunar.lunarInfo[lunarYear - 1899] & 0xf) == 0xf ? 30 : 29) : 0;
    }

    /**
     * ����ũ�����������
     * @param lunarYear ָ��ũ�����(����)
     * @return ��ũ�����������(����)
     */
    private static int getLunarYearDays(int lunarYear) {
        // ��С�¼���,ũ����������12 * 29 = 348��
        int daysInLunarYear = 348;
        // ���ݱ���,ÿ��ũ������16bit����ʾ,
        // ǰ12bit�ֱ��ʾ12���·ݵĴ�С��,���4bit��ʾ����
        // ÿ�������ۼ�һ��
        for (int i = 0x8000; i > 0x8; i >>= 1) {
            daysInLunarYear += ((Lunar.lunarInfo[lunarYear - 1900] & i) != 0) ? 1
                    : 0;
        }
        // ������������
        daysInLunarYear += Lunar.getLunarLeapDays(lunarYear);

        return daysInLunarYear;
    }

    /**
     * ����ũ���������·ݵ�������
     * @param lunarYear
     *            ָ��ũ�����(����)
     * @param lunarMonth
     *            ָ��ũ���·�(����)
     * @return ��ũ�������µ��·�(����,û�򷵻�0)
     */
    private static int getLunarMonthDays(int lunarYear, int lunarMonth) {
        // ���ݱ���,ÿ��ũ������16bit����ʾ,
        // ǰ12bit�ֱ��ʾ12���·ݵĴ�С��,���4bit��ʾ����
        int daysInLunarMonth = ((Lunar.lunarInfo[lunarYear - 1900] & (0x10000 >> lunarMonth)) != 0) ? 30
                : 29;
        return daysInLunarMonth;
    }
    /**
     * ȡ Date ��������ȫ���׼ʱ�� (UTC) ��ʾ������
     * 
     * @param date ָ������
     * @return UTC ȫ���׼ʱ�� (UTC) ��ʾ������
     */
    public static synchronized int getUTCDay(Date date) {
            Lunar.makeUTCCalendar();
            synchronized (utcCal) {
                utcCal.clear();
                utcCal.setTimeInMillis(date.getTime());
                return utcCal.get(Calendar.DAY_OF_MONTH);
            }
    }
    
    private static synchronized void makeUTCCalendar() {
        if (Lunar.utcCal == null) {
            Lunar.utcCal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        }
    }
    /**
     * ����ȫ���׼ʱ�� (UTC) (�� GMT) �� 1970 �� 1 �� 1 �յ���ָ������֮��������ĺ�������
     * @param y ָ�����
     * @param m ָ���·�
     * @param d ָ������
     * @param h ָ��Сʱ
     * @param min ָ������
     * @param sec ָ������
     * @return ȫ���׼ʱ�� (UTC) (�� GMT) �� 1970 �� 1 �� 1 �յ���ָ������֮��������ĺ�����
     */
    public static synchronized long UTC(int y, int m, int d, int h, int min, int sec) {
        Lunar.makeUTCCalendar();
        synchronized (utcCal) {
            utcCal.clear();
            utcCal.set(y, m, d, h, min, sec);
            return utcCal.getTimeInMillis();
        }
    }

    /**
     * ���ع��������������
     * @param solarYear ָ���������(����)
     * @param index ָ���������(����,0��С������)
     * @return ����(����,�����·ݵĵڼ���)
     */
    private static int getSolarTermDay(int solarYear, int index) {
        long l = (long)31556925974.7 * (solarYear - 1900) + solarTermInfo[index] * 60000L;
        l = l + Lunar.UTC(1900,0,6,2,5,0);
        return Lunar.getUTCDay(new Date(l));
    }

    public Lunar(int lang) {
        //��������������
    	switch(lang){
    		//��½����
	    	case 1:
	            Lunar.lFtv = new String[]{
	            	"",
	                "0101����", "0115Ԫ��", "0202��ͷ",
	                "0505����", "0707��Ϧ", "0715��Ԫ",
	                "0815����", "0909����", "1208����", 
	                "1230��Ϧ"
	            };
	        	Lunar.sFtv = new String[]{
	        		"0101Ԫ��", "0214���˽�", "0308��Ů��", "0312ֲ����", 
	        		"0401���˽�", "0501�Ͷ���", "0504�����", "0601��ͯ��", 
	        		"0701������", "0801������", "0910��ʦ��", "1001�����", 
	        		"1031��ʥ��", "1225ʥ����"
	        	};
	            break;
	        //̨�己��
	    	case 2:
	            Lunar.lFtv = new String[]{
	                "",
	                "0101����", "0115Ԫ��", "0202���^",
	                "0505����", "0707��Ϧ", "0715��Ԫ",
	                "0815����", "0909���", "1208�D��", 
	                "1230��Ϧ"
	            };
	        	Lunar.sFtv = new String[]{
			        "0101Ԫ��", "0214���˹�", "0228��ƽ�o����", "0308�DŮ��",
			        "0404��ͯ��", 	"0401���˹�", "0501�ڄӹ�", "0903܊�˹�", 
			        "0928�̎���", 	"1010���c��", "1031�f�}��", "1225Ү�Q��"
		        };
	            break;
	        //��۷���
	    	case 4:
	            Lunar.lFtv = new String[]{
	                "",
	                "0101����", "0115Ԫ��", "0202���^",
	                "0505����", "0707��Ϧ", "0715��Ԫ",
	                "0815����", "0909���", "1208�D��", 
	                "1230��Ϧ", "0408��"
	            };
	        	Lunar.sFtv = new String[]{
			        "0101Ԫ��", "0214���˹�", "0308�DŮ��", "0401���˹�",
			        "0501�ڄӹ�", "0701�؅^�o����", "0910�̎���", "1001���c��",
			        "1031�f�}��", "1225�}�Q��"
		        };
	            break;
	        //���ŷ���
	    	case 5:
	            Lunar.lFtv = new String[]{
	                "",
	                "0101����", "0115Ԫ��", "0202���^",
	                "0505����", "0707��Ϧ", "0715��Ԫ",
	                "0815����", "0909���", "1208�D��", 
	                "1230��Ϧ", "0408��"
	            };
	        	Lunar.sFtv = new String[]{
			        "0101Ԫ��", "0214���˹�", "0308�DŮ��", "0401���˹�",
			        "0501�ڄӹ�", "0910�̎���", "1001���c��", "1031�f�}��", 
			        "1102׷˼��", "1208�}ĸ�oԭ����", "1220�؅^�o����", "1225�}�Q��"
		        };
	            break;
	        //��½����
	    	case 6:
		        Lunar.lFtv = new String[]{
		          	"",
		            "0101����", "0115Ԫ��", "0202���^",
		            "0505����", "0707��Ϧ", "0715��Ԫ",
		            "0815����", "0909���", "1208�D��", 
		            "1230��Ϧ"
		        };
		        Lunar.sFtv = new String[]{
		        	"0101Ԫ��", "0214���˹�", "0308�DŮ��", "0312ֲ�九", 
		        	"0401���˹�", "0501�ڄӹ�", "0504���깝", "0601��ͯ��", 
		        	"0701���h��", "0801��܊��", "0910�̎���", "1001���c��", 
		        	"1031�f�}��", "1225Ү�Q��"
		        };
	            break;
	    	case 3:
	            Lunar.Deqi = new String[]{"","","","","","","","","","","",""};
	            Lunar.Animals = new String[]{"","","","","","","","","","","",""};
	            Lunar.solarTerm = new String[]{
	                "", "", "", "", "", "",
	                "", "", "", "", "", "",
	                "", "", "", "", "", "",
	                "", "", "", "", "", ""
	            };
	            Lunar.lunarString2 = new String[]{"","","","","","","",""};
	            Lunar.lFtv = new String[]{};
	            Lunar.sFtv = new String[]{};
	            Lunar.wFtv = new String[]{};
	            break;
        }
    	//�������Ӣ��
    	if(lang != 3){
    		//ѡ��С������
    		switch(Main._minor){
    			case 1: Lunar.lFtv[0] = "1223 С��";break;
    			case 2: Lunar.lFtv[0] = "1224 С��";break;
    			case 3: Lunar.lFtv[0] = "1225 С��";break;
    		}
    		//��ʼ���������ĵ�ͨ���ַ���
    		if(lang == 1){
	            Lunar.Deqi = new String[]{
		            "��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��"
		        };
		        Lunar.Animals = new String[]{
		            "��", "ţ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��"
		        };
		        Lunar.solarTerm = new String[]{
		            "С��", "��", "����", "��ˮ", "����", "����",
		            "����", "����", "����", "С��", "â��", "����",
		            "С��", "����", "����", "����", "��¶", "���",
		            "��¶", "˪��", "����", "Сѩ", "��ѩ", "����"
		        };
		        Lunar.lunarString2 = new String[]{
		            "��", "ʮ", "إ", "ئ", "��", "��", "��", "��"
		        };
	        	Lunar.wFtv = new String[]{
	        		"0521ĸ�׽�", "0631���׽�", "1145�ж���"
		        };
	        //��ʼ���������ĵ�ͨ���ַ���
    		}else{
                Lunar.Deqi = new String[]{
        		    "��", "�h", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��"
        		};
        		Lunar.Animals = new String[]{
        		    "��", "ţ", "��", "��", "��", "��", "�R", "��", "��", "�u", "��", "�i"
        		};
        		Lunar.solarTerm = new String[]{
        		    "С��", "��", "����", "��ˮ", "�@�U", "����",
        		    "����", "�Y��", "����", "С�M", "â�N", "����",
        		    "С��", "����", "����", "̎��", "��¶", "���",
        		    "��¶", "˪��", "����", "Сѩ", "��ѩ", "����"
        		};
        		Lunar.lunarString2 = new String[]{
        		    "��", "ʮ", "إ", "ئ", "��", "��", "�D", "�c"
        		};
		        Lunar.wFtv = new String[]{
			     	"0521ĸ�H��", "0631���H��", "1145�ж���"
			    };
    		}
    	}
    }

    public void init(long TimeInMillis) {
        lFestivalName = "";
        sFestivalName = "";
        termString = "";
        clFestivalName = "";
        csFestivalName = "";
        
        this.solar = Calendar.getInstance();
        this.solar.setTimeInMillis(TimeInMillis);
        Calendar baseDate = new GregorianCalendar(1900, 0, 31);
        long offset = (TimeInMillis - baseDate.getTimeInMillis()) / 86400000;
        // ��ũ����ݼ�ÿ���ũ��������ȷ��ũ�����
        this.lunarYear = 1900;
        int daysInLunarYear = Lunar.getLunarYearDays(this.lunarYear);
        while (this.lunarYear < 2100 && offset >= daysInLunarYear) {
            offset -= daysInLunarYear;
            daysInLunarYear = Lunar.getLunarYearDays(++this.lunarYear);
        }
        // ũ��������

        // ��ũ���µݼ�ÿ�µ�ũ��������ȷ��ũ���·�
        int lunarMonth = 1;
        // ����ũ�������ĸ���,��û�з���0
        int leapMonth = Lunar.getLunarLeapMonth(this.lunarYear);
        // �Ƿ�����
        this.isLeapYear = leapMonth > 0;
        // �����Ƿ�ݼ�
        boolean leapDec = false;
        boolean isLeap = false;
        int daysInLunarMonth = 0;
        while (lunarMonth<13 && offset>0) {
            if (isLeap && leapDec) { // ���������,����������
                // ����ũ�������µ�����
                daysInLunarMonth = Lunar.getLunarLeapDays(this.lunarYear);
                leapDec = false;
            } else {
                // ����ũ����ָ���µ�����
                daysInLunarMonth = Lunar.getLunarMonthDays(this.lunarYear, lunarMonth);
            }
            if (offset < daysInLunarMonth) {
                break;
            }
            offset -= daysInLunarMonth;

            if (leapMonth == lunarMonth && isLeap == false) {
                // �¸���������
                leapDec = true;
                isLeap = true;
            } else {
                // �·ݵ���
                lunarMonth++;
            }
        }
        // ũ��������
        this.lunarMonth = lunarMonth;
        // �Ƿ�����
        this.isLeap = (lunarMonth == leapMonth && isLeap);
        // ũ��������
        this.lunarDay = (int) offset + 1;
        // ȡ�ø�֧��
        this.getCyclicalData();
        
        findFestival();
    }
    
	/**
	 * ȡ�ù�����������
	 * @return ������������,������ǽ��շ��ؿմ�
	 */
	public String getSFestivalName() {
		return this.sFestivalName;
	}

    /**
     * ȡ��֧�� �������꣬���¸�֧�������й��Ĵ�����������ʼ�Ľ��£����й���̫��ʮ�����������ġ�
     * @param cncaData ��������(Tcnca)
     */
    private void getCyclicalData() {
        this.solarYear = this.solar.get(Calendar.YEAR);
        this.solarMonth = this.solar.get(Calendar.MONTH);
        this.solarDay = this.solar.get(Calendar.DAY_OF_MONTH);
        // ��֧��
        int cyclicalYear = 0;
        int cyclicalMonth = 0;
        int cyclicalDay = 0;

        // ��֧�� 1900��������Ϊ������(60����36)
        int term2 = Lunar.getSolarTermDay(solarYear, 2); // ��������
        // �������������·ֵ�����, ������Ϊ��
        if (solarMonth < 1 || (solarMonth == 1 && solarDay < term2)) {
            cyclicalYear = (solarYear - 1900 + 36 - 1) % 60;
        } else {
            cyclicalYear = (solarYear - 1900 + 36) % 60;
        }

        // ��֧�� 1900��1��С����ǰΪ ������(60����12)
        int firstNode = Lunar.getSolarTermDay(solarYear, solarMonth * 2); // ���ص��¡��ڡ�Ϊ���տ�ʼ
        // ����������, �ԡ��ڡ�Ϊ��
        if (solarDay < firstNode) {
            cyclicalMonth = ((solarYear - 1900) * 12 + solarMonth + 12) % 60;
        } else {
            cyclicalMonth = ((solarYear - 1900) * 12 + solarMonth + 13) % 60;
        }

        // ����һ���� 1900/1/1 �������
        // 1900/1/1�� 1970/1/1 ���25567��, 1900/1/1 ����Ϊ������(60����10)
        cyclicalDay = (int) (Lunar.UTC(solarYear, solarMonth, solarDay, 0, 0, 0) / 86400000 + 25567 + 10) % 60;
        this.cyclicalYear = cyclicalYear;
        this.cyclicalMonth = cyclicalMonth;
        this.cyclicalDay = cyclicalDay;
    }

    /**
     * ȡũ������Ф
     * @return ũ������Ф(��:��)
     */
    public String getAnimalString() {
        return Lunar.Animals[(this.lunarYear - 4) % 12];
    }

    /**
     * ���ع������ڵĽ����ַ���
     * @return ��ʮ�Ľ����ַ���,�����ǽ�����,���ؿմ�(��:����)
     */
    public String getTermString() {
        // ��ʮ�Ľ���
        if("".equals(this.termString)){
            this.termString = "";
            if (Lunar.getSolarTermDay(solarYear, solarMonth * 2) == solarDay) {
                this.termString = Lunar.solarTerm[solarMonth * 2];
            } else if (Lunar.getSolarTermDay(solarYear, solarMonth * 2 + 1) == solarDay) {
                this.termString = Lunar.solarTerm[solarMonth * 2 + 1];
            }
        }
        return this.termString;
    }
    
    /**
     * ������
     * @return ������
     */
    public int getTiananY() {
        return Lunar.getTianan(this.cyclicalYear);
    }

    /**
     * �·����
     * @return �·����
     */
    public int getTiananM() {
        return Lunar.getTianan(this.cyclicalMonth);
    }

    /**
     * �������
     * @return �������
     */
    public int getTiananD() {
        return Lunar.getTianan(this.cyclicalDay);
    }

    /**
     * ��ݵ�֧
     * @return ��ֵ�֧
     */
    public int getDeqiY() {
        return Lunar.getDeqi(this.cyclicalYear);
    }

    /**
     * �·ݵ�֧
     * @return �·ݵ�֧
     */
    public int getDeqiM() {
        return Lunar.getDeqi(this.cyclicalMonth);
    }

    /**
     * ���ڵ�֧
     * @return ���ڵ�֧
     */
    public int getDeqiD() {
        return Lunar.getDeqi(this.cyclicalDay);
    }

    /**
     * ȡ�ø�֧���ַ���
     * @return ��֧���ַ���
     */
    public String getCyclicaYear() {
        return this.getCyclicalString(this.cyclicalYear);
    }

    /**
     * ȡ�ø�֧���ַ���
     * @return ��֧���ַ���
     */
    public String getCyclicaMonth() {
        return this.getCyclicalString(this.cyclicalMonth);
    }

    /**
     * ȡ�ø�֧���ַ���
     * @return ��֧���ַ���
     */
    public String getCyclicaDay() {
        return this.getCyclicalString(this.cyclicalDay);
    }

    /**
     * ����ũ�������ַ���
     * @return ũ�������ַ���
     */
    public String getLunarDayString() {
        return this.getLunarDayString(this.lunarDay);
    }
    
    /**
     * ����ũ�������ַ���
     * @return ũ�������ַ���
     */
    public String getLunarMonthString() {
        return (this.isLeap() ? Lunar.lunarString2[7] : "") + this.getLunarMonthString(this.lunarMonth);
    }

    /**
     * ����ũ�������ַ���
     * @return ũ�������ַ���
     */
    public String getLunarYearString() {
        return this.getLunarYearString(this.lunarYear);
    }

    /**
     * ũ�����Ƿ�������
     * @return ũ�����Ƿ�������
     */
    public boolean isLeap() {
        return isLeap;
    }

    /**
     * ũ�����Ƿ�������
     * @return ũ�����Ƿ�������
     */
    public boolean isLeapYear() {
        return isLeapYear;
    }

    /**
     * ũ������
     * @return ũ������
     */
    public int getLunarDay() {
        return lunarDay;
    }

    /**
     * ũ���·�
     * @return ũ���·�
     */
    public int getLunarMonth() {
        return lunarMonth;
    }

    /**
     * ũ�����
     * @return ũ�����
     */
    public int getLunarYear() {
        return lunarYear;
    }

    /**
     * ȡ��ũ����������
     * @return ũ����������,������ǽ��շ��ؿմ�
     */
    public String getLFestivalName() {
        return this.lFestivalName;
    }
    
    /**
     * ȡ���Զ���ũ����������
     * @return ũ����������,������ǽ��շ��ؿմ�
     */
    public String getCLFestivalName() {
        return this.clFestivalName;
    }
    
    /**
     * ȡ���Զ��幫����������
     * @return ������������,������ǽ��շ��ؿմ�
     */
    public String getCSFestivalName() {
        return this.csFestivalName;
    }

	/**
	 * ��������
	 * @return ��������
	 */
	public int getSolarDay() {
		return solarDay;
	}

	/**
	 * �����·�
	 * @return �����·� (���Ǵ�0����)
	 */
	public int getSolarMonth() {
		return solarMonth+1;
	}

	/**
	 * �������
	 * @return �������
	 */
	public int getSolarYear() {
		return solarYear;
	}

    /**
     * ��֧�ַ���
     * @param cyclicalNumber ָ����֧λ��(����,0Ϊ����)
     * @return ��֧�ַ���
     */
    private String getCyclicalString(int cyclicalNumber) {
        return Lunar.Tianan[Lunar.getTianan(cyclicalNumber)] + Lunar.Deqi[Lunar.getDeqi(cyclicalNumber)];
    }

    /**
     * ��õ�֧
     * @param cyclicalNumber
     * @return ��֧ (����)
     */
    private static int getDeqi(int cyclicalNumber) {
         return cyclicalNumber % 12;
    }

    /**
     * ������
     * @param cyclicalNumber
     * @return ��� (����)
     */
    private static int getTianan(int cyclicalNumber) {
         return cyclicalNumber % 10;
    }

    /**
     * ����ָ�����ֵ�ũ����ݱ�ʾ�ַ���
     * @param lunarYear ũ�����(����,0Ϊ����)
     * @return ũ������ַ���
     */
    private String getLunarYearString(int lunarYear) {
        return this.getCyclicalString(lunarYear - 1900 + 36);
    }

    /**
     * ����ָ�����ֵ�ũ���·ݱ�ʾ�ַ���
     * @param lunarMonth ũ���·�(����)
     * @return ũ���·��ַ��� (��:��)
     */
    private String getLunarMonthString(int lunarMonth) {
        String lunarMonthString = "";
        if (lunarMonth == 1) {
            lunarMonthString = Lunar.lunarString2[4];
        }else if(lunarMonth == 10){
			lunarMonthString += Lunar.lunarString2[1];
		}else if(lunarMonth > 10){
			lunarMonthString += Lunar.lunarString2[lunarMonth % 10 + 4];
		}else if(lunarMonth % 10 > 0){
			lunarMonthString += Lunar.lunarString1[lunarMonth % 10];
        }
        return lunarMonthString;
    }
    
    /**
     * ����ָ�����ֵ�ũ���ձ�ʾ�ַ���
     * @param lunarDay ũ����(����)
     * @return ũ�����ַ��� (��: إһ)
     */
    private String getLunarDayString(int lunarDay) {
        if (lunarDay<1 || lunarDay>30) return "";
        int i1 = lunarDay / 10;
        int i2 = lunarDay % 10;
        String c1 = Lunar.lunarString2[i1];
        String c2 = Lunar.lunarString1[i2];
        if (lunarDay < 11) c1 = Lunar.lunarString2[0];
        if (i2 == 0) c2 = Lunar.lunarString2[1];
        return c1 + c2;
    }
}