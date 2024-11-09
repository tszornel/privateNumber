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

import com.ericsson.hosasdk.api.cc.TpCallEventInfo;
import com.ericsson.hosasdk.api.cc.TpReleaseCause;

import com.ericsson.hosasdk.api.cc.mpccs.IpAppCallLeg;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppCallLegAdapter;

/******************************************************************************
 * @see Callback
 ******************************************************************************/

public class LegCallback extends IpAppCallLegAdapter
    implements Callback, IpAppCallLeg
{
    private HelpdeskFeature theFeature;

    public LegCallback(HelpdeskFeature aFeature)
    {
        theFeature = aFeature;
    }
	
    public void eventReportRes(int callLegSessionID, TpCallEventInfo eventInfo)
    {
        theFeature.theMPCCSync.notifyResult(callLegSessionID,
            "Answered");
    }

    public void callLegEnded(int callLegSessionID, TpReleaseCause cause)
    {
        theFeature.theMPCCSync.notifyResult(callLegSessionID,
            "Leg ended");
    }

    // unused methods:
    // public void eventReportErr(int callLegSessionID, TpCallError errorIndication)
    // public void attachMediaRes(int callLegSessionID)
    // public void attachMediaErr(int callLegSessionID, TpCallError errorIndication)
    // public void detachMediaRes(int callLegSessionID)
    // public void detachMediaErr(int callLegSessionID, TpCallError errorIndication)
    // public void getInfoRes(int callLegSessionID, TpCallLegInfoReport callLegInfoReport)
    // public void getInfoErr(int callLegSessionID, TpCallError errorIndication)
    // public void routeErr(int callLegSessionID, TpCallError errorIndication)
    // public void superviseRes(int callLegSessionID, int report, int usedTime)
    // public void superviseErr(int callLegSessionID, TpCallError errorIndication)
}

