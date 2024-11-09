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

import java.util.*;

import com.ericsson.hosasdk.api.TpAddress;
import com.ericsson.hosasdk.api.TpAddressPlan;
import com.ericsson.hosasdk.api.TpAddressPresentation;
import com.ericsson.hosasdk.api.TpAddressRange;
import com.ericsson.hosasdk.api.TpAddressScreening;

import com.ericsson.hosasdk.api.cc.TpAdditionalCallEventCriteria;
import com.ericsson.hosasdk.api.cc.TpCallAppInfo;
import com.ericsson.hosasdk.api.cc.TpCallEventRequest;
import com.ericsson.hosasdk.api.cc.TpCallEventType;
import com.ericsson.hosasdk.api.cc.TpCallLegAttachMechanism;
import com.ericsson.hosasdk.api.cc.TpCallLegConnectionProperties;
import com.ericsson.hosasdk.api.cc.TpCallMonitorMode;
import com.ericsson.hosasdk.api.cc.TpCallNotificationInfo;
import com.ericsson.hosasdk.api.cc.TpCallNotificationRequest;
import com.ericsson.hosasdk.api.cc.TpCallNotificationScope;
import com.ericsson.hosasdk.api.cc.TpReleaseCause;

import com.ericsson.hosasdk.api.cc.mpccs.IpAppCallLeg;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppCallLegMgr;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCall;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCallControlManager;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCallControlManagerMgr;
import com.ericsson.hosasdk.api.cc.mpccs.IpAppMultiPartyCallMgr;
import com.ericsson.hosasdk.api.cc.mpccs.IpMultiPartyCall;
import com.ericsson.hosasdk.api.cc.mpccs.IpMultiPartyCallControlManager;
import com.ericsson.hosasdk.api.cc.mpccs.TpCallLegIdentifier;
import com.ericsson.hosasdk.api.cc.mpccs.TpMultiPartyCallIdentifier;

import com.ericsson.hosasdk.utility.framework.FWproxy;
import com.ericsson.hosasdk.utility.sync.Synchronizer;

/**
 * For a description of the behavior of this class, refer to the user guide.
 **/

public class HelpdeskFeature  
{
    // the FWProxy used to obtain the H-OSA service managers
    private FWproxy itsFramework;

    // the H-OSA service managers
    private IpMultiPartyCallControlManager itsMPCCManager;

    // the H-OSA callbacks 
    private IpAppMultiPartyCallControlManager itsMPCCManagerCallback;
    private IpAppMultiPartyCall theCallCallback;
    private IpAppCallLeg theLegCallback;

    // used by some of the callbacks to notify the HelpdeskFeature
    // that an important response was received.
    Synchronizer theMPCCSync = new Synchronizer();

    // some simple counters to demonstrate how statistics can be maintained
    private int theSuccessCounter, theTotalCounter;

    /**
     * The constructor
     *
     * @param aFramework The FWProxy that can be used to obtain service managers
     * @param aConfig Contains parameters and their values
     **/

    public HelpdeskFeature(FWproxy aFramework, Properties p)
    {
        itsFramework = aFramework;
    }

    /**
     * @see Feature#connect()
     **/

    protected void connect()
    {
        theCallCallback = new CallCallback(this);
        itsMPCCManagerCallback = new MPCCManagerCallback(this,
            theCallCallback);
        theLegCallback = new LegCallback(this);

        itsMPCCManager = (IpMultiPartyCallControlManager) itsFramework.obtainSCF(IpMultiPartyCallControlManager.class,
            "P_MULTI_PARTY_CALL_CONTROL");
        System.out.println("Connected to NRG");
    }

    /**
     * @see Feature#release()
     **/

    public void release()
    {
        System.out.println("Releasing from NRG");
        try
        { itsFramework.releaseSCF(itsMPCCManager);
        }
        finally  
        {	// even if disposing SCS resources fails, 
            // we still want to dispose of our resources

            IpAppCallLegMgr.dispose(theLegCallback);
            IpAppMultiPartyCallMgr.dispose(theCallCallback);
            IpAppMultiPartyCallControlManagerMgr.dispose(itsMPCCManagerCallback);
        }
    }

    /**
     * Activates a trigger to receive notifications whenever someone dials 
     * the service number. The notifications are received in interrupt mode, 
     * so that this feature (and not the network) can control the call.
     */

    public void activate(PrivateNumber aPrivateNumber)
    {
        // scope of notifications: anyone (*) calling the service number
        TpAddressRange origin = createE164Range("*");
        TpAddressRange destination = createE164Range(aPrivateNumber.toString());
        TpCallNotificationScope scope = new TpCallNotificationScope(destination,
            origin);

        // request to be notified when the destination number has just been dialled
        TpCallEventType eventType = TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED;
        TpAdditionalCallEventCriteria additionalCriteria = new TpAdditionalCallEventCriteria();
        additionalCriteria.MinAddressLength(1);
        additionalCriteria.MinAddressLength(0);

        eventType = TpCallEventType.P_CALL_EVENT_ADDRESS_ANALYSED;
        additionalCriteria = new TpAdditionalCallEventCriteria();
        additionalCriteria.Dummy((short) 0);

        // when the notification is received, this feature will take control the call
        TpCallMonitorMode mode = TpCallMonitorMode.P_CALL_MONITOR_MODE_INTERRUPT;

        // compose the notification request with the above data
        TpCallEventRequest request = new TpCallEventRequest(eventType,
            additionalCriteria, mode);
        TpCallEventRequest[] requests = {request};

        // activate the notification request 
        aPrivateNumber.theAssignmentId = itsMPCCManager.createNotification(itsMPCCManagerCallback,
            new TpCallNotificationRequest(scope, requests));
    }

    /**
     * Deactivates the trigger that was set with <code>startNotifications</code>.
     */

    public void deactivate(PrivateNumber aPrivateNumber)
    {
        itsMPCCManager.destroyNotification(aPrivateNumber.theAssignmentId);
    }

    /**
     *
     **/

    public void reportCallNotification(
        TpMultiPartyCallIdentifier aCall, 
        TpCallLegIdentifier[] aLegs, 
        TpCallNotificationInfo anInfo)
    {
        System.out.println("Accepting call from "
            + anInfo.CallNotificationReportScope.OriginatingAddress.AddrString);

        // for statistical purposes
        theTotalCounter++;

        // introduce a convenient name for the leg of the calling party
        TpCallLegIdentifier legA = aLegs[0];

        // request to be notified when the call is answered
        TpCallEventType eventType = TpCallEventType.P_CALL_EVENT_ANSWER;

        // a plain Answer event is sufficient, no additional criteria are needed
        TpAdditionalCallEventCriteria additionalCriteria = new TpAdditionalCallEventCriteria();
        additionalCriteria.Dummy((short) 0);

        // when this notification is received, this feature will take not control 
        TpCallMonitorMode mode = TpCallMonitorMode.P_CALL_MONITOR_MODE_NOTIFY;

        // compose the notification request with the above data
        TpCallEventRequest request = new TpCallEventRequest(eventType,
            additionalCriteria, mode);
        TpCallEventRequest[] requests = {request};

        String privateNumber = anInfo.CallNotificationReportScope.DestinationAddress.AddrString;
        String publicNumber = DataBase.INSTANCE.getPublicNumber(privateNumber);

        // create a terminating leg and request to be notified of the specified events
        TpCallLegIdentifier legB = aCall.CallReference.createAndRouteCallLegReq(aCall.CallSessionID,
            requests, createE164Address(publicNumber),
            anInfo.CallNotificationReportScope.OriginatingAddress,
            new TpCallAppInfo[0], theLegCallback);

        // let the network take it from here
        legA.CallLegReference.continueProcessing(legA.CallLegSessionID);

        // wait at most 90 seconds for the call to be answered
        Object routeResult = theMPCCSync.waitForResult(legB.CallLegSessionID,
            90000);

        // if the call is not answered in time (e.g. because either leg was abandoned,
        // or because a timeout occured), release the call
        if (!"Answered".equals(routeResult))
        {
            aCall.CallReference.release(aCall.CallSessionID,
                TpReleaseCause.P_UNDEFINED);
            return;
        }

        // we're no longer interested in the call
        aCall.CallReference.deassignCall(aCall.CallSessionID);

        System.out.println("Call from "
            + anInfo.CallNotificationReportScope.OriginatingAddress.AddrString
            + " was successful");

        // for statistical purposes
        theSuccessCounter++;
    }

    /**
     * Return a default TpAddress, based on an address string.
     **/

    public static TpAddress createE164Address(String aNumber)
    {
        return new TpAddress(TpAddressPlan.P_ADDRESS_PLAN_E164,
            aNumber, "",
            TpAddressPresentation.P_ADDRESS_PRESENTATION_UNDEFINED,
            TpAddressScreening.P_ADDRESS_SCREENING_UNDEFINED, "");
    }

    /**
     * Return a default TpAddressRange, based on an address string range.
     **/

    public static TpAddressRange createE164Range(String aNumberRange)
    {
        return new TpAddressRange(TpAddressPlan.P_ADDRESS_PLAN_E164,
            aNumberRange, // address
            "",  // name
            ""); // subaddress
    }
}
