/** ***********************************************************************
 * Freeware Licence Agreement
 *
 * This licence agreement only applies to the free version of this software.
 *
 * Terms and Conditions
 *
 * BY DOWNLOADING, INSTALLING, USING, TRANSMITTING, DISTRIBUTING OR COPYING THIS SOFTWARE ("THE SOFTWARE"), YOU AGREE TO THE TERMS OF THIS AGREEMENT (INCLUDING THE SOFTWARE LICENCE AND DISCLAIMER OF WARRANTY) WITH METAMUG THE OWNER OF ALL RIGHTS IN RESPECT OF THE SOFTWARE.
 *
 * PLEASE READ THIS DOCUMENT CAREFULLY BEFORE USING THE SOFTWARE.
 *
 * IF YOU DO NOT AGREE TO ANY OF THE TERMS OF THIS LICENCE THEN DO NOT DOWNLOAD, INSTALL, USE, TRANSMIT, DISTRIBUTE OR COPY THE SOFTWARE.
 *
 * THIS DOCUMENT CONSTITUTES A LICENCE TO USE THE SOFTWARE ON THE TERMS AND CONDITIONS APPEARING BELOW.
 *
 * The Software is licensed to you without charge for use only upon the terms of this licence, and METAMUG TECHNOLOGIES LLP (hereafter METAMUG) reserves all rights not expressly granted to you. METAMUG retains ownership of all copies of the Software.
 *
 * 1. Licence
 *
 * You may use the Software without charge.
 *
 * You may freely distribute exact copies of the Software to anyone.
 *
 * The inclusion of the Software in any Shareware, Freeware or similar media compilation or distribution method whereby it is made available at cost (ie. sold) is strictly prohibited.
 *
 * The selling of the Software is strictly prohibited.
 * 2. Restrictions
 *
 * METAMUG reserves the right to revoke the above distribution right at any time, for any or no reason.
 *
 * YOU MAY NOT MODIFY, ADAPT, TRANSLATE, RENT, LEASE, LOAN, SELL, ONSELL, REQUEST DONATIONS OR CREATE DERIVATIVE WORKS BASED UPON THE SOFTWARE OR ANY PART THEREOF.
 *
 * The Software contains intellectual property and to protect them you may not decompile, reverse engineer, disassemble or otherwise reduce the Software to a humanly perceivable form. You agree not to divulge, directly or indirectly, until such intellectual property cease to be confidential, for any reason not your own fault.
 *
 * 3. Termination
 *
 * This licence is effective until terminated. The Licence will terminate automatically without notice from METAMUG if you fail to comply with any provision of this Licence. Upon termination you must destroy the Software and all copies thereof. You may terminate this Licence at any time by destroying the Software and all copies thereof. Upon termination of this licence for any reason you shall continue to be bound by the provisions of Section 2 above. Termination will be without prejudice to any rights METAMUG may have as a result of this agreement.
 *
 * 4. Disclaimer of Warranty, Limitation of Remedies
 *
 * TO THE FULL EXTENT PERMITTED BY LAW, METAMUG HEREBY EXCLUDES ALL CONDITIONS AND WARRANTIES, WHETHER IMPOSED BY STATUTE OR BY OPERATION OF LAW OR OTHERWISE, NOT EXPRESSLY SET OUT HEREIN. THE SOFTWARE, AND ALL ACCOMPANYING FILES, DATA AND MATERIALS ARE DISTRIBUTED "AS IS" AND WITH NO WARRANTIES OF ANY KIND, WHETHER EXPRESS OR IMPLIED. METAMUG DOES NOT WARRANT, GUARANTEE OR MAKE ANY REPRESENTATIONS REGARDING THE USE, OR THE RESULTS OF THE USE, OF THE SOFTWARE WITH RESPECT TO ITS CORRECTNESS, ACCURACY, RELIABILITY, CURRENTNESS OR OTHERWISE. THE ENTIRE RISK OF USING THE SOFTWARE IS ASSUMED BY YOU. METAMUG MAKES NO EXPRESS OR IMPLIED WARRANTIES OR CONDITIONS INCLUDING, WITHOUT LIMITATION, THE WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. NO ORAL OR WRITTEN INFORMATION OR ADVICE GIVEN BY METAMUG, IT'S DISTRIBUTORS, AGENTS OR EMPLOYEES SHALL CREATE A WARRANTY, AND YOU MAY NOT RELY ON ANY SUCH INFORMATION OR ADVICE.
 *
 * IMPORTANT NOTE: Nothing in this Agreement is intended or shall be construed as excluding or modifying any statutory rights, warranties or conditions which by virtue of any national or state Fair Trading, Trade Practices or other such consumer legislation may not be modified or excluded. If permitted by such legislation, however, METAMUG's liability for any breach of any such warranty or condition shall be and is hereby limited to the supply of the Software licensed hereunder again as METAMUG at its sole discretion may determine to be necessary to correct the said breach.
 *
 * IN NO EVENT SHALL METAMUG BE LIABLE FOR ANY SPECIAL, INCIDENTAL, INDIRECT OR CONSEQUENTIAL DAMAGES (INCLUDING, WITHOUT LIMITATION, DAMAGES FOR LOSS OF BUSINESS PROFITS, BUSINESS INTERRUPTION, AND THE LOSS OF BUSINESS INFORMATION OR COMPUTER PROGRAMS), EVEN IF METAMUG OR ANY METAMUG REPRESENTATIVE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. IN ADDITION, IN NO EVENT DOES METAMUG AUTHORISE YOU TO USE THE SOFTWARE IN SITUATIONS WHERE FAILURE OF THE SOFTWARE TO PERFORM CAN REASONABLY BE EXPECTED TO RESULT IN A PHYSICAL INJURY, OR IN LOSS OF LIFE. ANY SUCH USE BY YOU IS ENTIRELY AT YOUR OWN RISK, AND YOU AGREE TO HOLD METAMUG HARMLESS FROM ANY CLAIMS OR LOSSES RELATING TO SUCH UNAUTHORISED USE.
 *
 * 5. General
 *
 * All rights of any kind in the Software which are not expressly granted in this Agreement are entirely and exclusively reserved to and by METAMUG.
 *
 * This Agreement shall be governed by the laws of the State of Maharastra, India. Exclusive jurisdiction and venue for all matters relating to this Agreement shall be in courts and fora located in the State of Maharastra, India, and you consent to such jurisdiction and venue. This agreement contains the entire Agreement between the parties hereto with respect to the subject matter hereof, and supersedes all prior agreements and/or understandings (oral or written). Failure or delay by METAMUG in enforcing any right or provision hereof shall not be deemed a waiver of such provision or right with respect to the instant or any subsequent breach. If any provision of this Agreement shall be held by a court of competent jurisdiction to be contrary to law, that provision will be enforced to the maximum extent permissible, and the remaining provisions of this Agreement will remain in force and effect.
 */
package com.metamug.api.taghandlers;

import com.metamug.api.common.MtgRequest;
import com.metamug.exec.RequestProcessable;
import com.metamug.exec.ResultProcessable;
import com.mtg.io.objectreturn.ObjectReturn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.sql.DataSource;
import javax.xml.bind.JAXBException;
import org.apache.taglibs.standard.tag.common.sql.ResultImpl;
import org.json.JSONObject;

/**
 *
 * @author Kainix
 */
public class CodeTagHandler extends BodyTagSupport implements TryCatchFinally {

    private String className;
    private Object param;
    private List<Object> parameters;
    @Resource(name = "jdbc/mtgMySQL")
    private DataSource ds;

    public CodeTagHandler() {
        super();
        init();
    }

    private void init() {
        className = null;
        param = null;
        parameters = null;
    }

    @Override
    public int doEndTag() throws JspException {
        JSONObject obj = new JSONObject();
        JspWriter out = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String acceptHeader = request.getHeader("Accept");
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        try {
            out = pageContext.getOut();
            Object result;
            Class cls = Class.forName((String) className);
            Object newInstance = cls.newInstance();
            ResultProcessable resProcessable;
            RequestProcessable reqProcessable;
            if (ResultProcessable.class.isAssignableFrom(cls)) {
                resProcessable = (ResultProcessable) newInstance;
                try {
                    if (param instanceof ResultImpl) {
                        ResultImpl ri = (ResultImpl) param;
                        result = resProcessable.process(ri.getRows(), ri.getColumnNames(), ri.getRowCount());

                        Object processedResult = ObjectReturn.convert(result, acceptHeader);

                        obj.put("result", processedResult);
                        out.print(obj);
                        pageContext.setAttribute("Content-Length", ((String) result).length(), PageContext.REQUEST_SCOPE);
                    }
                } catch (IOException | JAXBException ex) {
                    if (ex.getCause() != null) {
                        String cause = ex.getCause().toString();
                        obj.put("message", cause.split(": ")[1].replaceAll("(\\s|\\n|\\r|\\n\\r)+", " "));
                    } else {
                        obj.put("message", ex.getMessage().replaceAll("(\\s|\\n|\\r|\\n\\r)+", " "));
                    }
                    obj.put("status", 422);
                    response.setStatus(422);
                    out.print(obj);
                }
            } else if (RequestProcessable.class.isAssignableFrom(cls)) {
                reqProcessable = (RequestProcessable) newInstance;
                try {
                    if (param instanceof MtgRequest) {
                        MtgRequest mtg = (MtgRequest) param;
                        Enumeration<String> headerNames = request.getHeaderNames();
                        Map<String, String> requestHeaders = new HashMap<>();
                        while (headerNames.hasMoreElements()) {
                            String header = headerNames.nextElement();
                            requestHeaders.put(header, request.getHeader(header));
                        }
                        result = reqProcessable.process(mtg.getParams(), ds, requestHeaders);

                        Object processedResult = ObjectReturn.convert(result, acceptHeader);

                        obj.put("result", processedResult);
                        out.print(obj);
                        pageContext.setAttribute("Content-Length", ((String) result).length(), PageContext.REQUEST_SCOPE);
                    }
                } catch (IOException | JAXBException ex) {
                    if (ex.getCause() != null) {
                        String cause = ex.getCause().toString();
                        obj.put("message", cause.split(": ")[1].replaceAll("(\\s|\\n|\\r|\\n\\r)+", " "));
                    } else {
                        obj.put("message", ex.getMessage().replaceAll("(\\s|\\n|\\r|\\n\\r)+", " "));
                    }
                    obj.put("status", 422);
                    response.setStatus(422);
                }
            } else {
                obj.put("message", "Class isn't processable");
                obj.put("status", 422);
                response.setStatus(422);
                out.print(obj);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | SecurityException | IllegalArgumentException | IOException ex) {
            if (ex.getClass().toString().contains("AccessControlException")) {
                obj.put("message", "Access denied, can't access system information.");
                obj.put("status", 403);
                response.setStatus(403);
            } else {
                if (ex.getCause() != null) {
                    String cause = ex.getCause().toString();
                    obj.put("message", cause.split(": ")[1].replaceAll("(\\s|\\n|\\r|\\n\\r)+", " "));
                } else {
                    obj.put("message", ex.getMessage().replaceAll("(\\s|\\n|\\r|\\n\\r)+", " "));
                }
                obj.put("status", 422);
                response.setStatus(422);
            }
            Logger.getLogger(CodeTagHandler.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        return EVAL_PAGE;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setParam(Object Parameter) {
        this.param = Parameter;
    }

    /**
     * Just rethrows the Throwable.
     *
     * @param throwable
     * @throws java.lang.Throwable
     */
    @Override
    public void doCatch(Throwable throwable) throws Throwable {
        throw throwable;
    }

    /**
     * Close the <code>Connection</code>, unless this action is used as part of a transaction.
     */
    @Override
    public void doFinally() {
        className = null;
        param = null;
        parameters = null;
        ds = null;
    }

    public void addParameter(Object obj) {
        if (parameters == null) {
            parameters = new ArrayList<>();
        }
        parameters.add(obj);
    }
}