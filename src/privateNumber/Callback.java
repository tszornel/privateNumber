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

/******************************************************************************/
//
// Classes that implement this interface provide callbacks required by the 
// HelpdeskFeature:
//
// - MPCCManagerCallback implements IpAppMultiPartyCallControlManager
// - CallCallback        implements IpAppMultiPartyCall
// - LegCallback         implements IpAppCallLeg
// - UIManagerCallback   implements IpAppUIManager
// - UICallCallback      implements IpAppUICall
//
// The methods of these classes can be subdivided into the following categories:
//
// 1) methods that cannot be invoked (illegal flow)
// 2) methods that that are usual to be invoked (normal flow)
// 3) methods that are possible yet unusual to be invoked (exceptional flow)
//
// An example of category 1 is superviseRes. It cannot be invoked because
// the corresponsing method superviseReq is never invoked by the application.
//
// An example of category 2 is eventReportRes. It's expected to be invoked
// because the application invokes eventReportReq, requesting an invocation
// of eventReportRes whenever a call is answered.
//
// An example of category 3 is eventReportErr. It can be invoked because
// the application invokes eventReportReq. However, this would indicate
// a deviation from the usual flow of events.
//
// Note that this categorisation is highly dependent on the application
// that uses the callbacks. Methods that cannot be invoked in this application
// can very well be invoked in other applications, and vice versa.
// The categorisation also depends on the definition of 
// 'usual flow of events', i.e. the distinction between normal flow and
// exceptional flow is based on design choices, which may differ per
// application.
//
// Whenever a category 1 method is invoked (which should not happen),
// it is ignored.
//
// Whenever a category 2 method is invoked, the application is notified.
//
// Whenever a category 3 method is invoked, it is ignored.
// This will generally result in an exception being thrown, e.g. because 
// of a timeout in the application. Other applications may want
// handle some or all category 3 methods in more advanced manners.
//
/******************************************************************************/

public interface Callback
{}
