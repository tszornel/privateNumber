/*
 * **************************************************************************
 * *                                                                        *
 * * Ericsson hereby grants to the user a royalty-free, irrevocable,        *
 * * worldwide, nonexclusive, paid-up license to copy, display, perform,    *
 * * prepare and have prepared derivative works based upon the source code  *
 * * in this sample application, and distribute the sample source code and  *
 * * derivative works thereof and to grant others the foregoing rights.     *
 * *                                                                        *
 * * ERICSSON DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,        *
 * * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.       *
 * * IN NO EVENT SHALL ERICSSON BE LIABLE FOR ANY SPECIAL, INDIRECT OR      *
 * * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS    *
 * * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE  *
 * * OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE *
 * * OR PERFORMANCE OF THIS SOFTWARE.                                       *
 * *                                                                        *
 * **************************************************************************
 */


package privateNumber;

import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCall;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCallAdapter;

/******************************************************************************
 * @see Callback
 ******************************************************************************/

public class CallCallback extends IpAppMultiPartyCallAdapter
    implements Callback, IpAppMultiPartyCall 
{
    public CallCallback(HelpdeskFeature aFeature)
    {}

    // unused methods:
    // public void getInfoRes(int callSessionID, TpCallInfoReport callInfoReport)
    // public void getInfoErr(int callSessionID, TpCallError errorIndication)
    // public void superviseRes(int callSessionID, int report, int usedTime)
    // public void superviseErr(int callSessionID, TpCallError errorIndication)
    // public void callEnded(int callSessionID, TpCallEndedReport report)
    // public void createAndRouteCallLegErr(int callSessionID, TpCallLegIdentifier callLegReference, TpCallError errorIndication)
}

