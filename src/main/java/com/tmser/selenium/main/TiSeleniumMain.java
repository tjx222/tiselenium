package com.tmser.selenium.main;

import com.tmser.selenium.tools.RandomArc;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class TiSeleniumMain {
    static {
        String property = System.getProperty("webdriver.edge.driver", null);
        if (Objects.isNull(property)) {
            System.setProperty("webdriver.edge.driver", "C:/webdriver/msedgedriver.exe");
        }
    }

    static String[] parse(){
        String s ="4d6963726f736f667420546169204c65, 696e646578656444624b6579, 6f70657261, 6879706f74, 636c69636b, 746f7563687374617274, 246368726f6d655f6173796e63536372697074496e666f, 636c6561724361636865, 736c696365, 616363656c65726174696f6e496e636c7564696e6747726176697479, 6e6176696761746f72, 616c6c, 2c226175746822203a2022, 6765745f73746f705f7369676e616c73, 746f, 4d656e6c6f, 70616374, 6b6579646f776e, 697349676e, 736561726368, 67657453746f7261676555706461746573, 417269616c4865627265772d4c69676874, 666f6e7446616d696c79, 776562647269766572, 746d655f636e74, 726571756573744d656469614b657953797374656d416363657373, 427566666572, 686f73746e616d65, 63737354657874, 6e5f636b, 6964, 6b655f76656c, 467574757261, 504f5354, 2d312c322c2d39342c2d3132302c, 64617461, 6c69737446756e6374696f6e73, 646d615f7468726f74746c65, 4d6163204f5320582031305f35, 6c616e6775616765, 436f7572696572204e6577, 726762283132302c203138362c2031373629, 626f6479, 57696e646f7773204d6564696120506c6179657220506c75672d696e2044796e616d6963204c696e6b204c696272617279, 2d312c322c2d39342c2d38302c, 6170706c79, 576562457836342047656e6572616c20506c7567696e20436f6e7461696e6572, 76635f636e74, 2c697430, 6766, 69313a, 7061676558, 4c61746f, 5f7365744175, 73657373696f6e53746f726167654b6579, 4c6f6273746572, 646973467043616c4f6e54696d656f7574, 7365745f636f6f6b6965, 574542474c5f64656275675f72656e64657265725f696e666f, 7374726f6b655374796c65, 436f727369766120486562726577, 72434650, 646f63756d656e74456c656d656e74, 5f5f7765626472697665725f756e77726170706564, 2c73323a, 6d6e5f73746f7574, 7265717569726564, 7061727365496e74, 2d312c322c2d39342c2d3130362c, 5f736574506f775374617465, 72616e646f6d, 7061796d656e742d68616e646c6572, 5072657373205374617274203250, 6264, 696e6465784f66, 66696c6c54657874, 416374697665584f626a656374, 636f6f6b6965, 627574746f6e, 6d6e5f72, 6d6e5f6d635f6c6d74, 6d616374, 6e6f77, 706c7567696e496e666f, 246364635f6173646a666c617375746f70666876635a4c6d63666c5f, 756e646566, 6361746368, 7931, 6d6f7a496e6e657253637265656e59, 6163636573736962696c6974792d6576656e7473, 676574417474726962757465, 726f746174696f6e52617465, 74696d657a6f6e654f66667365744b6579, 5f5f6c6173745761746972436f6e6669726d, 6764, 2d312c322c2d39342c2d3132392c, 7461, 584d4c4874747052657175657374, 636f6c6f724465707468, 666f6e7453697a65, 656d6974, 6765745f636f6f6b6965, 70757368, 6d647563655f636e74, 7769647468, 6d7348696464656e, 7669736962696c6974796368616e6765, 7765626b69747669736962696c6974796368616e6765, 706f736974696f6e3a2072656c61746976653b206c6566743a202d3939393970783b207669736962696c6974793a2068696464656e3b20646973706c61793a20626c6f636b2021696d706f7274616e74, 2d31, 627064, 73656e64, 726f746174655f7269676874, 6f6e726561647973746174656368616e6765, 5f5f6c6173745761746972416c657274, 7832, 6d6963726f70686f6e65, 6666, 73657452657175657374486561646572, 7765626b697447657447616d6570616473, 6d6e5f6364, 745f646973, 626d697363, 76635f636e745f6c6d74, 70726f746f636f6c, 746f75636863616e63656c, 7175657279, 4a61766120506c75672d696e203220666f72204e504150492042726f7773657273, 687663, 4368726f6d652052656d6f7465204465736b746f7020566965776572, 6170695f7075626c69635f6b6579, 61757468, 5f5f6472697665725f6576616c75617465, 2c73313a, 32, 7772, 76696272617465, 666d68, 6765745f63665f64617465, 666f6e74, 666f6e74735f6f70746d, 6d6e5f706f6c6c, 2c6c6f633a, 6d647563655f636e745f6c6d74, 70656e, 73657373696f6e53746f72616765, 646f655f636e74, 636b6965, 6765745f62726f77736572, 637265617465456c656d656e74, 666f6e7473, 6d6e5f6363, 646f5f646973, 5f5f66786472697665725f6576616c75617465, 616a5f696e6478, 667056616c737472, 6762, 4142434445464748494a4b4c4d4e4f505152535455565758595a6162636465666768696a6b6c6d6e6f707172737475767778797a303132333435363738392b2f, 6c6f63616c53746f726167654b6579, 6861734f776e50726f7065727479, 61706963616c6c5f626d, 686273, 3d3d, 6c7663, 706f77, 2d312c322c2d39342c2d3131392c, 70655f76656c, 6f6e666f637573, 646f615f7468726f74746c65, 61665362657038796a6e5a556a7133614c3031306a4f31355361776a32565a6664594b3875593930757871, 47656e657661, 636c69656e745769647468, 63646f61, 7b2273657373696f6e5f696422203a2022, 7765627274634b6579, 64656661756c745f73657373696f6e, 686d6d, 6365696c, 2d312c322c2d39342c2d3130352c, 6a6f696e, 5f7068616e746f6d, 616a5f6c6d745f74616374, 23663630, 6d6e5f6765745f6e65775f6368616c6c656e67655f706172616d73, 7374617274547261636b696e67, 6b6579436f6465, 617661696c5769647468, 3b20706174683d2f3b20657870697265733d4672692c2030312046656220323032352030383a30303a303020474d543b, 686d64, 63646d61, 616273, 736564, 3136707420417269616c, 68696464656e, 746f4c6f77657243617365, 66706366, 706c7567696e73, 5850617468526573756c74, 2f6765745f706172616d73, 696e69745f74696d65, 6d6e5f77, 73706c6974, 65, 5f5f247765626472697665724173796e634578656375746f72, 6f6666, 667056616c, 6f66667365745769647468, 70657273697374656e742d73746f72616765, 746f4461746155524c, 78616767, 646f63756d656e74, 696e666f726d696e666f, 426173696320, 646d655f636e745f6c6d74, 696e6e6572486569676874, 687463, 70647563655f636e74, 746f4669786564, 6972, 737472696e67696679, 776562676c, 636c6970626f617264, 4465766963654f7269656e746174696f6e4576656e74, 6d61676e65746f6d65746572, 3061343647356d3137567270346f3463, 64656661756c7456616c7565, 2c, 61626364656668696a6b6c6d6e6f7071727374757678797a313233343536373839303b2b2d2e, 6d6f7a49734c6f63616c6c79417661696c61626c65, 417574686f72697a6174696f6e, 4170706c6520476f74686963, 2d, 657863657074696f6e, 696d756c, 426174616e67, 73746f726557656257696465547261636b696e67457863657074696f6e, 70617470, 63616c6c53656c656e69756d, 7374726f6b65, 33, 745f747374, 687064, 4d6f6e61636f, 637368, 2d312c322c2d39342c2d3132362c, 2c73333a, 57696e6764696e67732032, 636b61, 616c706861, 6d72, 30, 68746d, 6164644576656e744c697374656e6572, 737065616b6572, 696e707574, 6e6f6e3a, 636c6970626f6172642d7772697465, 646d5f646973, 646f4e6f74547261636b, 2d312c322c2d39342c2d3130382c, 4d6963726f736f66742053616e73205365726966, 736634, 6364635f61646f51706f61736e666137367066635a4c6d63666c5f53796d626f6c, 73746172745f7473, 676574506172616d65746572, 526f626f746f, 6d657373616765, 6d6564696144657669636573, 66697273744c6f6164, 3a, 646973, 686b75, 5368617265506f696e742042726f7773657220506c75672d696e, 6772616e746564, 646f6d4175746f6d6174696f6e436f6e74726f6c6c6572, 677764, 6c616e67, 5c5c22, 63726564656e7469616c73, 2d312c322c2d39342c2d3131382c, 5f5f7765626472697665725f7363726970745f66756e6374696f6e, 6f6e4c696e65, 646f6d4175746f6d6174696f6e, 6c6f6170, 2d312c322c2d39342c2d3130392c, 726972, 2d312c322c2d39342c2d3132322c, 63616c5f646973, 6373, 687075, 416c4e696c65, 746172676574, 6d6d655f636e74, 44656661756c742042726f777365722048656c706572, 636d61, 474554, 706f696e74657254797065, 4c75636964612053616e73, 695061643b, 5f736574426d, 616c744b6579, 6d6e5f727473, 6779726f73636f7065, 646576696365506978656c526174696f, 6d6e5f7374617465, 6765745f74797065, 5c27, 6f6e, 73746f72616765, 726762283130322c203230342c203029, 727665, 4e696d62757320526f6d616e204e6f2039204c, 706f696e746572646f776e, 646976, 636861724174, 636c6970626f6172642d72656164, 73717274, 776174696e45787072657373696f6e526573756c74, 72656d6f76654368696c64, 2d312c322c2d39342c2d3130312c, 6d6f7a48696464656e, 4465766963654d6f74696f6e4576656e74, 6d6e5f74636c, 7663616374, 636865636b5f73746f705f70726f746f636f6c, 3139327078, 56657273696f6e2f342e30, 2d312c322c2d39342c2d3131312c, 6d737669736962696c6974796368616e6765, 5f5f7765626472697665725f7363726970745f66756e63, 646f616374, 67657447616d6570616473, 546f7563684576656e74, 7464, 6f6e6b65797570, 6170706c7946756e63, 55524c, 686d75, 73616e732d7365726966, 676574456c656d656e747342795461674e616d65, 73656e64426561636f6e, 6d6f7a416c61726d73, 616363656c65726f6d65746572, 2c6370656e3a, 64656e696564, 656e4765744c6f63, 4170706c65204c69476f74686963, 2d312c322c2d39342c2d3131302c, 7468656e, 57617365656d, 746f756368656e64, 7370616e, 66696c6c52656374, 2d312c322c2d39342c2d3132312c, 227d, 73706c696365, 2d312c322c2d39342c2d3131362c, 686173496e64657865644442, 7769746843726564656e7469616c73, 6863, 6261636b67726f756e642d73796e63, 2d312c322c2d39342c2d37302c, 6a7273, 7374796c65, 4176656e6972, 5049, 676574666f726d696e666f, 74647563655f636e745f6c6d74, 656e636f6465, 77676c, 6d6e5f6162636b, 6364635f61646f51706f61736e666137367066635a4c6d63666c5f4172726179, 6d6e5f6765745f63757272656e745f6368616c6c656e676573, 4e617469766520436c69656e74, 6c617374496e6465784f66, 617765736f6d69756d, 73686966744b6579, 4368726f6d652050444620566965776572, 31, 6f64, 6f6e6b65797072657373, 4f70656e2053616e73, 63616e766173, 737562737472696e67, 7374617274646f61646d61, 2f2f, 6d6e5f6374, 686e, , 7a31, 7069, 687473, 6f6e706f696e746572646f776e, 6d6e5f6c63, 64656e, 6d6e5f73656e, 4e6f746f, 5f53656c656e69756d5f4944455f5265636f72646572, 2c6d6e5f773a, 5f736574467370, 73657373696f6e5f6964, 70617373776f7264, 756e646566696e6564, 6b655f636e745f6c6d74, 686b64, 46616e7461737175652053616e73204d6f6e6f, 6d6e5f7072, 70726f746f74797065, 756172, 6261743a, 736372697074, 7831, 67656f6c6f636174696f6e, 6364635f61646f51706f61736e666137367066635a4c6d63666c5f50726f6d697365, 73706565636853796e746865736973, 67657442617474657279, 63655f6a735f706f7374, 6d6e5f746f7574, 6d6d655f636e745f6c6d74, 6d6e5f6d635f696e6478, 636c69656e7458, 7a, 75726c, 5f6162636b, 75706461746574, 436f6e74656e742d74797065, 646d616374, 737263, 6377656e3a, 70726f64756374537562, 686b70, 5f, 6b616374, 7b2273656e736f725f64617461223a22, 73645f6465627567, 4d6963726f736f6674204f6666696365204c69766520506c75672d696e, 616a5f696e64785f646f616374, 72656164795374617465, 617661696c486569676874, 616363656c65726174696f6e, 687474703a2f2f, 70655f636e74, 46696c65526561646572, 6c6f63, 6173696e, 2d312c322c2d39342c2d3131322c, 616a5f74797065, 536f757263652053616e732050726f, 6862, 736f7274, 7663, 2c73373a, 73633a, 746f456c656d656e74, 636f6f6b6965456e61626c6564, 7061727365, 39307078, 7365726966, 676574436f6e74657874, 63616c635f6670, 5769646576696e6520436f6e74656e742044656372797074696f6e204d6f64756c65, 7e, 6d6e5f6c64, 6950686f6e65, 41646f626520427261696c6c65, 646f655f76656c, 676562, 4e657720596f726b, 70647563655f636e745f6c6d74, 73637265656e, 2c7365745344464e3a, 4f7377616c64, 3264, 776c, 6973633a, 67616d6d61, 726567697374657250726f746f636f6c48616e646c6572, 7772633a, 6170706c69636174696f6e2f6a736f6e, 6d6e5f7774, 504c5547494e53, 6d734d616e6970756c6174696f6e5669657773456e61626c6564, 7364666e, 43656e7475727920476f74686963, 706172616d735f75726c, 6765745f6d6e5f706172616d735f66726f6d5f6162636b, 5f5f7068616e746f6d6173, 6d2c457621785636374261553e206568326d3c663341473340, 2d312c322c2d39342c2d3132372c, 697354727573746564, 616a5f7373, 536872656520446576616e616761726920373134, 436f6e7374727563746f72, 616a5f696e64785f74616374, 62646d, 41646f6265204163726f626174, 6f6e626c7572, 4f534d4a4946, 6973206e6f7420612076616c696420656e756d2076616c7565206f662074797065205065726d697373696f6e4e616d65, 7669623a, 53696c7665726c6967687420506c75672d496e, 5f5f77656264726976657246756e63676562, 3c2f7365745344464e3e, 737061776e, 7064, 636c69656e74486569676874, 776568, 456467652050444620566965776572, 2d312c322c2d39342c2d3132342c, 436f7572696572, 6d6e5f696c, 646d655f76656c, 4a617661204170706c657420506c75672d696e, 63686b6e756c6c, 22, 6f39, 666f7245616368, 5f5f66786472697665725f756e77726170706564, 7265706c616365, 745f656e, 4d534945, 726f756e64, 6b65797570, 6e6f74696669636174696f6e73, 6e61765f7065726d, 73657276696365576f726b6572, 2f5f626d2f5f64617461, 6c6f636174696f6e, 2c30, 737368, 676574566f69636573, 636c69656e7459, 6d6e5f73, 63616d657261, 43656e74757279, 656e52656164446f6355726c, 6f6e636c69636b, 50617079727573, 616d6269656e742d6c696768742d73656e736f72, 727374, 3b20, 6e756d626572, 636e73, 3c6270643e, 637461, 6174746163684576656e74, 5f5f7765626472697665725f6576616c75617465, 756e6b, 68747470733a, 5765624b69742d696e74656772696572746520504446, 6f6e6b6579646f776e, 6d696469, 6d6f7a50686f6e654e756d62657253657276696365, 6d6e5f7570646174655f6368616c6c656e67655f64657461696c73, 66756e6374696f6e, 666d, 676574457874656e73696f6e, 6d6f7a436f6e6e656374696f6e, 617263, 2d312c322c2d39342c2d3130332c, 41646f626541414d446574656374, 426972636820537464, 426f646f6e69203732, 7368696674, 6d6574614b6579, 766572, 43616e746172656c6c, 6d6f7573656d6f7665, 74655f636e74, 74655f76656c, 6465766963656d6f74696f6e, 554e4d41534b45445f52454e44455245525f574542474c, 637061, 70726f64756374, 5f5f6472697665725f756e77726170706564, 6d6e5f6c67, 6d6f7a52544350656572436f6e6e656374696f6e, 4d6f6e6f7370616365, 6a617661456e61626c6564, 43616e64617261, 66696c6c5374796c65, 6c6f63616c53746f72616765, 6432, 6d6e5f7473, 666d7a, 6d6e5f616c, 6465766963656f7269656e746174696f6e, 50616c6174696e6f2d426f6c64, 5265616c506c617965722056657273696f6e20506c7567696e, 70617273655f6770, 7765626b697452544350656572436f6e6e656374696f6e, 73656c656e69756d, 6861726477617265436f6e63757272656e6379, 4d6f7a696c6c612044656661756c7420506c75672d696e, 48544d4c456c656d656e74, 6e, 50616c6174696e6f, 6a735f706f7374, 666964636e74, 6d655f636e74, 7768696368, 61636f73, 6d6f757365646f776e, 67657453746174654669656c64, 49544320426f646f6e6920373220426f6c64, 6f6e6d6f7573656d6f7665, 77656273746f7265, 6f6e6d6f7573657570, 554e4d41534b45445f56454e444f525f574542474c, 6866, 5f5f6c617374576174697250726f6d7074, 70726f6d7074, 6d6170, 656d61696c, 72756e466f6e7473, 696e6e65725769647468, 7265717565737457616b654c6f636b, 36707420417269616c, 43616c69627269, 616a5f6c6d745f646d616374, 63616c6c656453656c656e69756d, 6e6f6e65, 6d6e5f7274, 6d6f757365, 666173, 3c406e7634352e2046316e3633722c5072316e37316e3621, 68747470733a2f2f, 7776, 7256616c, 6f6666736574486569676874, 696e6e657248544d4c, 6433, 4176656e6972204e657874, 44616d6173637573, 616a5f696e64785f646d616374, 70737562, 706d655f636e745f6c6d74, 6263, 54492d4e7370697265, 616a5f6c6d745f646f616374, 3b, 53686f636b7761766520666f72204469726563746f72, 6174616e68, 70726576666964, 5562756e7475204d656469756d, 6363, 687465, 656e41646448696464656e, 7765626b697454656d706f7261727953746f72616765, 67627276, 76616c7565, 706f696e7465727570, 63616c6c65645068616e746f6d, 6368726f6d65, 686569676874, 63616c6c, 6f70633a, 2d312c322c2d39342c2d3132332c, 517569636b73616e64, 6861734c6f63616c53746f72616765, 746d655f636e745f6c6d74, 436f6d6963204e657565, 5f5f73656c656e69756d5f756e77726170706564, 737461636b, 63665f75726c, 3c7365745344464e3e, 6162, 626d, 5f5f7765626472697665725f5f636872, 646d655f636e74, 74616374, 6374726c4b6579, 617473, 476f6f676c652054616c6b20506c7567696e20566964656f2052656e6465726572, 7831323a, 63616c6c5068616e746f6d, 5365726966, 6950686f746f50686f746f63617374, 74656c, 2d312c322c2d39342c2d3130302c, 44726f6964205365726966, 666d6765745f74617267657473, 6175746f636f6d706c657465, 776174696e45787072657373696f6e4572726f72, 646973706c6179, 696e73, 48656c766574696361204e657565, 746f537472696e67, 7061676559, 70726f64, 646d3a, 6f6e6c6f6164, 646f63756d656e744d6f6465, 7831313a, 706d655f636e74, 52544350656572436f6e6e656374696f6e, 676574456c656d656e7442794964, 627276, 7370796e6e65725f6164646974696f6e616c5f6a735f6c6f61646564, 7d, 6e616d65, 68617353657373696f6e53746f72616765, 62657461, 707374617465, 657870, 58446f6d61696e52657175657374, 696e64657865644442, 616374697665456c656d656e74, 556e69747920506c61796572, 53686f636b7761766520466c617368, 63757272656e74536372697074, 43616d62726961, 6d655f76656c, 667056616c43616c63756c61746564, 2d312c322c2d39342c2d3131372c, 6272617665, 656e636f64655f75746638, 73656e736f725f64617461, 7374617465, 517569636b54696d6520506c75672d696e, 646373, 6e70, 706978656c4465707468, 62, 646f5f656e, 706c656e, 757365724167656e74, 6f70656e, 6f757465725769647468, 616c74466f6e7473, 6368696c644e6f646573, 646d5f656e, 506f696e7465724576656e74, 676574466c6f617456616c, 5562756e747520526567756c6172, 42656c6c204d54, 636f6f6b69655f63686b5f72656164, 686243616c63, 737472696e67, 47696c6c2053616e73204d54, 747374, 6465766963652d696e666f, 6d6e5f6c636c, 617070656e644368696c64, 666c6f6f72, 74797065, 63627274, 54696d6573, 5265616c506c6179657228746d29204732204c697665436f6e6e6563742d456e61626c656420506c75672d496e202833322d62697429, 6d6d6d6d6d6d6d6d6c6c69, 79, 5f5f7765626472697665725f7363726970745f666e, 6361636865, 626d2d74656c656d65747279, 6d6f7573657570, 676574456c656d656e747342794e616d65, 596f755475626520506c75672d696e, 67657454696d657a6f6e654f6666736574, 647269766572, 6d6f7a7669736962696c6974796368616e6765, 6f6e706f696e7465727570, 5f5f73656c656e69756d5f6576616c75617465, 2c7561656e642c, 69734272617665, 3c696e69742f3e, 766f696365555249, 3d, 2f, 667370, 6b65797072657373, 7065726d697373696f6e73, 6765746d72, 6765746475726c, 2d312c322c2d39342c2d3130322c, 6170704d696e6f7256657273696f6e, 7765626b697448696464656e, 63686172436f64654174, 6d6e5f70736e, 74647563655f636e74, 63686172436f6465, 646f61646d615f656e, 6d6f6e6f7370616365, 6d6e5f696e6974, 64656661756c74, 6165696f75793133353739, 62696e64, 2d312c322c2d39342c2d3131342c, 77656e, 74657874, 222c2273656e736f725f6461746122203a2022, 6576656e74, 61707056657273696f6e, 78, 41646f626520486562726577, 2d312c322c2d39342c2d3131352c, 6f6e6d6f757365646f776e, 646f655f636e745f6c6d74, 5f5f6e696768746d617265, 6c656e677468, 746f7563686d6f7665, 4d6963726f736f66742e584d4c48545450, 7c7c, 476f6f676c6520456172746820506c75672d696e, 4d696e696f6e2050726f, 66726f6d43686172436f6465, 66633a, 676574537570706f72746564457874656e73696f6e73, 626c7565746f6f7468, 6b655f636e74, 3c2f6270643e";
        String[] ss = s.split(",");
        int index = 0;
        for (String sk : ss){
            ss[index++] = fromHexString(sk);
        }

        return ss;

    }

    /**
     * 16进制直接转换成为字符串
     * @explain
     * @param hexString 16进制字符串
     * @return String （字符集：UTF-8）
     */
    public static String fromHexString(String hexString){
        // 用于接收转换结果
        String result = "";
        // 转大写
        hexString = hexString.trim().toUpperCase();
        // 16进制字符
        String hexDigital = "0123456789ABCDEF";
        // 将16进制字符串转换成char数组
        char[] hexs = hexString.toCharArray();
        // 能被16整除，肯定可以被2整除
        byte[] bytes = new byte[hexString.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = hexDigital.indexOf(hexs[2 * i]) * 16 + hexDigital.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        // byte[]--&gt;String
        result = new String(bytes, Charset.forName("ISO-8859-1"));
        return result;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
       // replaece();
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("headless");
        /*WebDriver driver = new EdgeDriver();

        driver.get("https://www.ti.com.cn/zh-cn/ordering-resources/buying-tools/quick-add-to-cart.html");
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        wait.until(numberOfWindowsToBe(1));
        System.out.println("=====" + driver.getTitle());
       // checkLogin(driver, wait);

        String[] codes = new String[]{"XLV320ADC6120IRTER"};
        int i= 1;
        for (String code : codes){
            try {
                loadStorage(wait, driver, code, i++);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }


        WebElement button = wait.until(d -> d.findElement(By.cssSelector("div.ti-quick-add-to-cart-worksheet-actions ti-button")));
        Thread.sleep(200);
        button.click();
        checkLogin(driver);

        Thread.sleep(20000);
        choseCountry(driver);

        toOrder(driver);

        address(driver);
        bill(driver);
        safe(driver);
        send(driver);
        pay(driver);

        Thread.sleep(10000);*/
        // base64Decode();
    }

    private static void checkLogin(WebDriver driver) throws InterruptedException {
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(60))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
//        WebElement login = driver.findElement(By.cssSelector("div.ti_p-responsiveHeader-top-bar ti-login"));
//        action.moveToElement(login).click().perform();
        //login.click();
        WebElement username = wait.until(d -> d.findElement(By.name("username")));
        username.click();
        username.sendKeys("tjx1222@163.com");
        WebElement nextbutton = wait.until(d -> d.findElement(By.name("nextbutton")));
        //nextbutton.sendKeys(Keys.ENTER);
        nextbutton.click();
        WebElement password = wait.until(d -> d.findElement(By.name("password")));
        password.click();

        Action keydown = action.keyDown(Keys.SHIFT).sendKeys("T").keyUp(Keys.SHIFT).sendKeys("i")
                .keyDown(Keys.SHIFT).sendKeys("-").keyUp(Keys.SHIFT)
                .sendKeys(Keys.NUMPAD1).sendKeys(Keys.NUMPAD2).sendKeys(Keys.NUMPAD3).sendKeys(Keys.NUMPAD4)
                .sendKeys(Keys.NUMPAD5).sendKeys(Keys.NUMPAD6).sendKeys(Keys.NUMPAD7).sendKeys(Keys.NUMPAD8).build();
        keydown.perform();
        WebElement loginbutton = wait.until(d -> d.findElement(By.name("loginbutton")));
        Thread.sleep(200);
        loginbutton.click();
        Thread.sleep(3000);

    }

    private static void choseCountry(WebDriver driver) throws InterruptedException {
        System.out.println("start----- chose country");
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement selector = wait.until(d -> d.findElement(By.id("llc-cartpage-ship-to-country")));
        action.moveToElement(selector).click().perform();
        Thread.sleep(50);

        //login.click();
        Select s = new Select(selector);
        s.selectByValue("CN");
        Thread.sleep(20);
        WebElement nextbutton = wait.until(d -> d.findElement(By.id("llc-cartpage-ship-to-continue")));
        action.moveToElement(nextbutton).click().perform();

//        WebElement button = wait.until(d -> d.findElement(By.cssSelector("ul.ti-header-language-selection-list li[data-value='zh-CN'] a")));
//        action.moveToElement(button).click().perform();

        Thread.sleep(3000);
    }


    //下单
    private static void toOrder(WebDriver driver) throws InterruptedException {
        System.out.println("start----- got to order");
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(60))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement button = wait.until(d -> d.findElement(By.id("tiCartCalculate_Checkout_top")));
        action.moveByOffset(10,30).moveByOffset(200,400).perform();
        action.moveToElement(button).perform();
        Thread.sleep(80);
        button.click();
    }


    //下单
    private static void address(WebDriver driver) throws InterruptedException {
        System.out.println("start----- address");
        Actions action = new Actions(driver);


        boolean first = true;
        while (true){
            try {
                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                        .withTimeout(Duration.ofSeconds(120))
                        .pollingEvery(Duration.ofSeconds(5))
                        .ignoring(NoSuchElementException.class);
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("paid-shipping-address-select")));
                action.moveByOffset(100,50).moveByOffset(200,455).perform();
                action.moveToElement(button).perform();
                Thread.sleep(47);
                button.click();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                if(first){
                    first = false;
                    //toOrder(driver);
                }

                if(!first){
                    break;
                }
            }
        }
        Thread.sleep(2000);
    }

    //发票
    private static void bill(WebDriver driver) throws InterruptedException {
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement checkboxflag = wait.until(d -> d.findElement(By.id("checkboxflag")));
        action.moveByOffset(100,50).moveByOffset(400,500).perform();
        action.moveToElement(checkboxflag).perform();
        Thread.sleep(80);
        checkboxflag.click();

        WebElement checkButton = wait.until(d -> d.findElement(By.id("tax-invoice-submit")));
        action.moveToElement(checkButton).perform();
        Thread.sleep(89);
        checkButton.click();

        WebElement submit = wait.until(d -> d.findElement(By.cssSelector("div.ti_p-button-set button[type='submit']")));
        action.moveToElement(submit).perform();
        Thread.sleep(189);
        submit.click();
        Thread.sleep(2000);
    }


    //合规
    private static void safe(WebDriver driver) throws InterruptedException {
        System.out.println("start----- safe check");
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement selector = wait.until(d -> d.findElement(By.id("regulations-select")));
        action.moveToElement(selector).perform();
        Thread.sleep(50);
        selector.click();

        Select s = new Select(selector);
        s.selectByIndex(0);

        WebElement nextSelector = wait.until(d -> d.findElement(By.id("checkout-regulations-select")));
        action.moveToElement(nextSelector).perform();
        Thread.sleep(50);
        nextSelector.click();

        Select ns = new Select(nextSelector);
        ns.selectByIndex(0);

        WebElement checkboxflag = wait.until(d -> d.findElement(By.id("military-flag")));
        action.moveToElement(checkboxflag).click().perform();

        WebElement nextButton = wait.until(d -> d.findElement(By.id("regulations-submit-btn")));
        action.moveToElement(nextButton).click().perform();

        Thread.sleep(2000);
    }


    //配送
    private static void send(WebDriver driver) throws InterruptedException {
        System.out.println("start----- send");
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement checkboxflag = wait.until(d -> d.findElement(By.id("terms_accept")));
        action.moveToElement(checkboxflag).click().perform();

        WebElement nextButton = wait.until(d -> d.findElement(By.id("shipping-method-submit")));
        action.moveToElement(nextButton).click().perform();

        Thread.sleep(2000);
    }


    //支付
    private static void pay(WebDriver driver) throws InterruptedException {
        System.out.println("start----- pay");
        Actions action = new Actions(driver);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(60))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        WebElement checkboxflag = wait.until(d -> d.findElement(By.id("payment-method-alipay")));
        action.moveToElement(checkboxflag).click().perform();

        WebElement nextButton = wait.until(d -> d.findElement(By.cssSelector("button.js-apm-paymentbtn")));
        action.moveToElement(nextButton).click().perform();

        Thread.sleep(2000);
    }



    private static void loadStorage(Wait<WebDriver> wait, WebDriver driver, String code, int line) {
        while (true){
            WebElement input = wait.until(d -> d.findElement(By.xpath("//tbody/tr["+line+"]/td[2]/ti-input")));
            WebElement input1 = wait.until(d -> d.findElement(By.xpath("//tbody/tr["+line+"]/td[4]/ti-input")));
            WebElement input3 = wait.until(d -> d.findElement(By.xpath("//tbody/tr["+line+"]/td[6]")));
            // String js = "document.getElementsByTagName(\"tbody\")[0].children[0].children[1].children[0].shadowRoot.children[0].value=\"PDRV8220DRLR\";";
            //Object o = ((EdgeDriver) driver).executeScript(js);
            //WebElement input = foo.findElement(By.xpath("//input"));
            Actions action = new Actions(driver);
            action.moveToElement(input).click().perform();
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    try {
                        action.moveToElement(input).click().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                        input.sendKeys(code);
                        return true;
                    } catch (StaleElementReferenceException e) {
                        return null;
                    }
                }
            });

            action.moveToElement(input1).click().perform();
            input1.sendKeys(Keys.NUMPAD1);
            action.moveToElement(input3).click().perform();
            try {
            String text = wait.until(new ExpectedCondition<String>() {
                WebElement element = driver.findElement(By.xpath("//tbody/tr["+line+"]/td[5]"));
                private String elementText;

                @Override
                public String apply(WebDriver driver) {
                    try {
                        elementText = element.getText();
                        return elementText != null && elementText.length() > 0 ? elementText : null;
                    } catch (StaleElementReferenceException e) {
                        return null;
                    }
                }

                @Override
                public String toString() {
                    return String.format("text ('%s') to be present in element %s", elementText, String.valueOf(element));
                }
            });

            input1.click();
            input1.sendKeys(Keys.NUMPAD1);
            System.out.println("---------------"+ text);
            } catch (TimeoutException e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }


    static void base64Decode() {
/*
        String s = "bm9uZQ==,cG9zdE1lc3NhZ2U=,c3RyaW5naWZ5,b3Blbg==,R0VU,d2l0aENyZWRlbnRpYWxz,c2VuZA==,aW5kZXhPZg==,c3Vic3RyaW5n,cHJvdG90eXBl,cmVhZHlzdGF0ZWNoYW5nZQ==,c3RhdHVz,c2V0UmVxdWVzdEhlYWRlcg==,WC1TZWMtQ2xnZS1SZXEtVHlwZQ==,YWpheA==,ZnVuY3Rpb24=,ZmV0Y2g=,YXBwbHk=,dGhlbg==,Y2xvbmU=,bWVzc2FnZQ==,ZGF0YQ==,Y2FwdGNoYV9yZXNwb25zZQ==,c3RhdGVfcmVzcG9uc2U=,YXR0YWNoRXZlbnQ=,b2JqZWN0,bG9jYXRpb24=,cHJvdG9jb2w=,YmxvYg==,cmVzcG9uc2VUeXBl,YWRkRXZlbnRMaXN0ZW5lcg==,bG9hZGVuZA==,cGFyc2U=,cmVhZEFzVGV4dA==,cmVzcG9uc2U=,anNvbg==,cmVzcG9uc2VUZXh0,c2VjLWNwLWNoYWxsZW5nZQ==,dHJ1ZQ==,c2VjLWNvbnRhaW5lcg==,Zmlyc3RDaGlsZA==,cmVtb3ZlQ2hpbGQ=,Y3JlYXRlRWxlbWVudA==,aWZyYW1l,c2V0QXR0cmlidXRl,c2VjLWlmLWNvbnRhaW5lcg==,c2VjLWNwdC1pZg==,ZnJhbWVCb3JkZXI=,c2Nyb2xsaW5n,Y2xhc3M=,cHJvdmlkZXI=,ZGF0YS1ob3N0bmFtZQ==,aGFzT3duUHJvcGVydHk=,YnJhbmRpbmdfdHlwZQ==,Y3VzdG9tX2JyYW5kaW5n,YnJhbmRpbmdfY3VzdF91cmw=,Y3VzdG9tLWJyYW5kaW5n,Y3J5cHRv,P2R1cmF0aW9uPQ==,Y2hsZ19kdXJhdGlvbg==,ZGF0YS1kdXJhdGlvbg==,c2V0SXRlbQ==,c3Jj,ZGF0YS1rZXk=,cHJvdmlkZXJfc2VjcmV0X3B1YmxpYw==,YXBwZW5kQ2hpbGQ=,ZGl2,c2VjLXRleHQtaWY=,YnJhbmRpbmdfdXJsX2NvbnRlbnQ=,L19zZWMvY3BfY2hhbGxlbmdlL2FrLWNoYWxsZW5nZS0zLTYuaHRt,L19zZWMvY3BfY2hhbGxlbmdlLw==,Z2V0RWxlbWVudEJ5SWQ=,c2VjLW92ZXJsYXk=,c3R5bGU=,ZGlzcGxheQ==,YmxvY2s=,c3BsaXQ=,aG9zdG5hbWU=,Z2V0QXR0cmlidXRl,cmVhZHlTdGF0ZQ==,c3VjY2Vzcw==";
        String[] sarr = s.split(",");
        for (String str : sarr) {
            System.out.println(new String(Base64.getDecoder().decode(str)));
        }
*/

   //     replaece();
    }

    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    static void replaece() throws IOException {
        String[] attr = parse();

        String content = FileCopyUtils.copyToString(new FileReader(new File("E:\\ti.js")));
        Pattern p = Pattern.compile("_ac\\[(\\d+)\\]");
        Matcher m = p.matcher(content);
        StringBuffer sb = new StringBuffer();
        int c = 0;
        while (m.find()) {
            String index = m.group(1);
            int i = Integer.parseInt(index);
            c++;
            String rs =  attr[i];
            rs = rs.replaceAll("\\$", "\\\\\\$");
            m.appendReplacement(sb, "'" + rs + "'");
        }
        m.appendTail(sb);

        System.out.println(sb);
    }
}
