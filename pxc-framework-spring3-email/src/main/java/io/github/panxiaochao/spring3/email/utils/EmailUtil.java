/*
 * Copyright © 2024-2025 Lypxc(潘) (545685602@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.panxiaochao.spring3.email.utils;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import io.github.panxiaochao.spring3.core.utils.CharPools;
import io.github.panxiaochao.spring3.core.utils.MapUtil;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.core.utils.StrUtil;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 邮箱工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-09-07
 */
public class EmailUtil {

    private static final MailAccount ACCOUNT = SpringContextUtil.getBean(MailAccount.class);

    private EmailUtil() {
    }

    /**
     * 获取邮件发送实例
     */
    public static MailAccount getMailAccount() {
        return ACCOUNT;
    }

    /**
     * 获取邮件发送实例 (自定义发送人以及授权码)
     *
     * @param user 发送人
     * @param pass 授权码
     */
    public static MailAccount getMailAccount(String from, String user, String pass) {
        ACCOUNT.setFrom(StrUtil.defaultIfBlank(from, ACCOUNT.getFrom()));
        ACCOUNT.setUser(StrUtil.defaultIfBlank(user, ACCOUNT.getUser()));
        ACCOUNT.setPass(StrUtil.defaultIfBlank(pass, ACCOUNT.getPass()));
        return ACCOUNT;
    }

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    public static String sendText(String to, String subject, String content, File... files) {
        return send(to, subject, content, false, files);
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    public static String sendHtml(String to, String subject, String content, File... files) {
        return send(to, subject, content, true, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     */
    public static String send(String to, String subject, String content, boolean isHtml, File... files) {
        return send(splitAddress(to), subject, content, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
     * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param cc      抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param bcc     密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     */
    public static String send(String to, String cc, String bcc, String subject, String content, boolean isHtml,
                              File... files) {
        return send(splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    public static String sendText(Collection<String> tos, String subject, String content, File... files) {
        return send(tos, subject, content, false, files);
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    public static String sendHtml(Collection<String> tos, String subject, String content, File... files) {
        return send(tos, subject, content, true, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     */
    public static String send(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
        return send(tos, null, null, subject, content, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param ccs     抄送人列表，可以为null或空
     * @param bccs    密送人列表，可以为null或空
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     */
    public static String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject,
                              String content, boolean isHtml, File... files) {
        return send(getMailAccount(), true, tos, ccs, bccs, subject, content, null, isHtml, files);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件认证对象
     * @param to          收件人，多个收件人逗号或者分号隔开
     * @param subject     标题
     * @param content     正文
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     */
    public static String send(MailAccount mailAccount, String to, String subject, String content, boolean isHtml,
                              File... files) {
        return send(mailAccount, splitAddress(to), subject, content, isHtml, files);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param subject     标题
     * @param content     正文
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     */
    public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content,
                              boolean isHtml, File... files) {
        return send(mailAccount, tos, null, null, subject, content, isHtml, files);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param ccs         抄送人列表，可以为null或空
     * @param bccs        密送人列表，可以为null或空
     * @param subject     标题
     * @param content     正文
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     */
    public static String send(MailAccount mailAccount, Collection<String> tos, Collection<String> ccs,
                              Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
        return send(mailAccount, false, tos, ccs, bccs, subject, content, null, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files    附件列表
     * @return message-id
     */
    public static String sendHtml(String to, String subject, String content, Map<String, InputStream> imageMap,
                                  File... files) {
        return send(to, subject, content, imageMap, true, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     */
    public static String send(String to, String subject, String content, Map<String, InputStream> imageMap,
                              boolean isHtml, File... files) {
        return send(splitAddress(to), subject, content, imageMap, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
     * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param cc       抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param bcc      密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     */
    public static String send(String to, String cc, String bcc, String subject, String content,
                              Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, imageMap, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files    附件列表
     * @return message-id
     */
    public static String sendHtml(Collection<String> tos, String subject, String content,
                                  Map<String, InputStream> imageMap, File... files) {
        return send(tos, subject, content, imageMap, true, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     */
    public static String send(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap,
                              boolean isHtml, File... files) {
        return send(tos, null, null, subject, content, imageMap, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param ccs      抄送人列表，可以为null或空
     * @param bccs     密送人列表，可以为null或空
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     */
    public static String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject,
                              String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(getMailAccount(), true, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件认证对象
     * @param to          收件人，多个收件人逗号或者分号隔开
     * @param subject     标题
     * @param content     正文
     * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     */
    public static String send(MailAccount mailAccount, String to, String subject, String content,
                              Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(mailAccount, splitAddress(to), subject, content, imageMap, isHtml, files);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param subject     标题
     * @param content     正文
     * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     */
    public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content,
                              Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return send(mailAccount, tos, null, null, subject, content, imageMap, isHtml, files);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param ccs         抄送人列表，可以为null或空
     * @param bccs        密送人列表，可以为null或空
     * @param subject     标题
     * @param content     正文
     * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     */
    public static String send(MailAccount mailAccount, Collection<String> tos, Collection<String> ccs,
                              Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml,
                              File... files) {
        return send(mailAccount, false, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
    }

    /**
     * 根据配置文件，获取邮件客户端会话
     *
     * @param mailAccount 邮件账户配置
     * @param isSingleton 是否单例（全局共享会话）
     * @return {@link Session}
     */
    public static Session getSession(MailAccount mailAccount, boolean isSingleton) {
        Authenticator authenticator = null;
        if (mailAccount.isAuth()) {
            authenticator = new UserPassAuthenticator(mailAccount.getUser(), mailAccount.getPass());
        }
        return isSingleton ? Session.getDefaultInstance(mailAccount.getSmtpProps(), authenticator) //
                : Session.getInstance(mailAccount.getSmtpProps(), authenticator);
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount      邮件帐户信息
     * @param useGlobalSession 是否全局共享Session
     * @param tos              收件人列表
     * @param ccs              抄送人列表，可以为null或空
     * @param bccs             密送人列表，可以为null或空
     * @param subject          标题
     * @param content          正文
     * @param imageMap         图片与占位符，占位符格式为cid:${cid}
     * @param isHtml           是否为HTML格式
     * @param files            附件列表
     * @return message-id
     */
    private static String send(MailAccount mailAccount, boolean useGlobalSession, Collection<String> tos,
                               Collection<String> ccs, Collection<String> bccs, String subject, String content,
                               Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        final Mail mail = Mail.create(mailAccount).setUseGlobalSession(useGlobalSession);

        // 可选抄送人
        if (!CollectionUtils.isEmpty(ccs)) {
            mail.setCcs(ccs.toArray(new String[0]));
        }
        // 可选密送人
        if (!CollectionUtils.isEmpty((bccs))) {
            mail.setBccs(bccs.toArray(new String[0]));
        }

        mail.setTos(tos.toArray(new String[0]));
        mail.setTitle(subject);
        mail.setContent(content);
        mail.setHtml(isHtml);
        mail.setFiles(files);

        // 图片
        if (MapUtil.isNotEmpty(imageMap)) {
            for (Map.Entry<String, InputStream> entry : imageMap.entrySet()) {
                mail.addImage(entry.getKey(), entry.getValue());
                // 关闭流
                close(entry.getValue());
            }
        }

        return mail.send();
    }

    /**
     * 关闭流，关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    /**
     * 将多个联系人转为列表，分隔符为逗号或者分号
     *
     * @param addresses 多个联系人，如果为空返回null
     * @return 联系人列表
     */
    private static List<String> splitAddress(String addresses) {
        if (StrUtil.isBlank(addresses)) {
            return null;
        }

        List<String> result;
        if (StrUtil.contains(addresses, CharPools.COMMA)) {
            result = Arrays.asList(StrUtil.split(addresses, CharPools.COMMA));
        } else if (StrUtil.contains(addresses, CharPools.COLON)) {
            result = Arrays.asList(StrUtil.split(addresses, CharPools.COLON));
        } else {
            result = Collections.singletonList(addresses);
        }
        return result;
    }

    /**
     * 用户名密码验证器
     */
    static class UserPassAuthenticator extends Authenticator {
        private final String user;
        private final String pass;

        /**
         * 构造
         *
         * @param user 用户名
         * @param pass 密码
         */
        public UserPassAuthenticator(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.pass);
        }
    }

}
