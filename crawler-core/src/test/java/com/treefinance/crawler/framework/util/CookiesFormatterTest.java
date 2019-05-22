/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.treefinance.crawler.framework.util;

import com.treefinance.toolkit.util.Strings;
import org.junit.Test;

import java.util.Map;

/**
 * @author Jerry
 * @date 2019-05-22 19:22
 */
public class CookiesFormatterTest {

    @Test
    public void parseAsMap() {
        String cookies = "ALIPAYJSESSIONID=RZ11PRBFSNCFmX9litG0E3NWd7paTwauthRZ11GZ00;"
            + "CHAIR_SESS=JWYmdXvINYrjfJhNfnAOAgXN2lTyJ7veaMyDOlqV8dWzNpJknIpzFUa26WlSqle4crTRVSYVgV_UBr5hAHPPbWoeijIV4ReeWGbiuKIdPeERtYyuQdo1bwHC5O6eNAvIjpAaIo83kWPMT7ZMk4qTE_crxzJuuQWrrZ0qCjuAvAmvjH8X1lKWIdeaJ4VmDRxcyLtStY8BNTG2J7iPBp6ihx_ONNluR5Dp2LtsUhbSoccca9sanq-cMhRYTm83Vq5C;CLUB_ALIPAY_COM=2088202440742366;JSESSIONID=RZ11PRBFSNCFmX9litG0E3NWd7paTwauthRZ11GZ00;LoginForm=trust_login_taobao;_cc_=VT5L2FSpdA%3D%3D;_l_g_=Ug%3D%3D;_m_h5_tk=3522a0a268b1fda1c78ce0f9ce59dee8_1550675043312;_m_h5_tk_enc=3dc47ddd983ab21faea86ab1292f22d3;_mw_us_time_=1550667125839;_nk_=%5Cu5929%5Cu5992%5Cu7D2B%5Cu51B0;_tb_token_=f773b8ebb15e4;_uab_collina=155066711151520123495269;ali_apache_tracktmp=uid=2088202440742366;alipay=K1iSL1vgJ4arX8lKfgUAYBR6R5CY71mEeTHQtTb2jqJ/W26HtvHSUqw=;cna=Zzv0FPvG3FYCAXQ+eNXABMgt;cookie1=UUjTSNclpskoZW2uLa2Z%2BvQXwtjuCO3hQSteLhB2Zzg%3D;cookie17=UNDUKres6nA2;cookie2=10454c14987fa2d911c71366a70399d5;cookieCheck=21864;csg=6c49debe;ctoken=iWZnUw5qMdlQip9_;dnk=%5Cu5929%5Cu5992%5Cu7D2B%5Cu51B0;existShop=MTU1MDY2NzExNQ%3D%3D;isg=BNjYdxl1MNYDfhxSj9KVEXpoqgCqafcRUOI5dBLJJJPGrXiXutEM2-6P4SU4pvQj;iw.nick=Jva2hcOz;iw.partner=Lfo3AA==;iw.userid=K1iSL1vgJ4arX8lKfgUAYA==;lc=VypRGeZafTRg8CdHUQ%3D%3D;lgc=%5Cu5929%5Cu5992%5Cu7D2B%5Cu51B0;lid=%E5%A4%A9%E5%A6%92%E7%B4%AB%E5%86%B0;log=lty=UQ%3D%3D;mt=np=;publishItemObj=Ng%3D%3D;session.cookieNameId=ALIPAYJSESSIONID;sg=%E5%86%B04a;skt=5c65cc78af28ebaa;spanner=EQvl1u4c2xlK82Ra2mljNB1bVD0QwwfaXt2T4qEYgj0=;t=0c2e04d862b2901bd2d36d6eec688087;tbcp=f=UUjZel8%2FHoXKxgPW03Tc90jXvRc%3D&e=VynJ%2FlLyhEuUovNWGYGVCQ%3D%3D;tg=0;tracknick=%5Cu5929%5Cu5992%5Cu7D2B%5Cu51B0;uc1=cookie16=U%2BGCWk%2F74Mx5tgzv3dWpnhjPaQ%3D%3D&cookie21=UtASsssme%2BBq&cookie15=V32FPkk%2Fw0dUvg%3D%3D&existShop=false&pas=0&cookie14=UoTZ5Oa2mqK93g%3D%3D&tag=8&lng=zh_CN;uc3=vt3=F8dByEzc6LbU7GvWKWI%3D&id2=UNDUKres6nA2&nk2=r7lQMdIz9Uk%3D&lg2=U%2BGCWk%2F75gdr5Q%3D%3D;ucn=center;umt=C2b1630ce1764af265a35091000847d2f;unb=300581884;v=0;zone=GZ00D";

        Map<String, String> cookieMap = CookiesFormatter.parseAsMap(cookies);

        System.out.println(cookieMap);

        String[] cookieArray = CookiesFormatter.toCookieArray(cookieMap, false);
        System.out.println(Strings.join(cookieArray, ";"));

        cookieMap = CookiesFormatter.parseAsMap(cookieArray, false);

        System.out.println(cookieMap);

        cookieArray = CookiesFormatter.parseAsArray(cookieArray, false);

        System.out.println(Strings.join(cookieArray, ";"));
    }
}