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

import com.ericsson.hosasdk.api.cc.TpCallNotificationInfo;

import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCall;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCallControlManager;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCallControlManagerAdapter;
import com.ericsson.hosasdk.api.cc.mpccs.TpAppMultiPartyCallBack;
import com.ericsson.hosasdk.api.cc.mpccs.TpCallLegIdentifier;
import com.ericsson.hosasdk.api.cc.mpccs.TpMultiPartyCallIdentifier;
import com.ericsson.hosasdk.api.SDKCommunicationException;
import com.ericsson.hosasdk.utility.sync.ThreadPool;

/******************************************************************************
 * @see Callback
 ******************************************************************************/

public class MPCCManagerCallback extends IpAppMultiPartyCallControlManagerAdapter
    implements Callback, IpAppMultiPartyCallControlManager
{
    // the feature that receives events of interest received by this callback
    private HelpdeskFeature theFeature;

    // the same callback will be used for all calls 
    TpAppMultiPartyCallBack theCallCallback;

    // allows us to handle calls in separate yet re-usable threads
    private ThreadPool itsThreadPool;

    public MPCCManagerCallback(HelpdeskFeature aFeature, IpAppMultiPartyCall aCallCallback)
    {
        theFeature = aFeature;
        theCallCallback = new TpAppMultiPartyCallBack();
        theCallCallback.AppMultiPartyCall(aCallCallback);
        itsThreadPool = new ThreadPool();
    }

    public TpAppMultiPartyCallBack reportNotification(
        final TpMultiPartyCallIdentifier call, 
        final TpCallLegIdentifier[] legs, 
        final TpCallNotificationInfo anInfo, 
        int assignmentID)
    {
        // we want to return the callback for the call to the SCS as soon as possible,
        // whitout blocking this method until the call has been handled.
        // For reasons of efficiency, we don't want to create a new thread 
        // for each and every new call. A ThreadPool allows us the 
        // handle the call is a separate, re-usable thread.
        itsThreadPool.start(new Runnable()
        {
            public void run()
            {
                try
                {
                    theFeature.reportCallNotification(call, legs,
                        anInfo);
                }
                catch (SDKCommunicationException e)
                {// (ignored)
                    // this exception may be thrown if the application uses an interface
                    // that is no longer available (typically because the call has been
                    // abandonded
                }
                catch (RuntimeException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return theCallCallback;
    }

    // unused methods:
    // public void callAborted(int callReference)
    // public void managerInterrupted()
    // public void managerResumed()
    // public void callOverloadEncountered(int assignmentID)
    // public void callOverloadCeased(int assignmentID)
}

