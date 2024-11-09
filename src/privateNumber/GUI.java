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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

public class GUI extends JPanel
{
    DataPanel2 panel;

    public GUI(Main aMain)
    {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        add(new JLabel(new ImageIcon("images/banner.png")),
            BorderLayout.NORTH);
        add(panel = new DataPanel2(aMain, "1",
            DataBase.INSTANCE.getPrivateNumbers("1")),
            BorderLayout.CENTER);
        add(new JLabel(new ImageIcon("images/scene.gif")),
            BorderLayout.EAST);
    }

    void setActivated(boolean b)
    {
        panel.setActivated(b);
    }
} class PrivateNumber
{
    String theNumber;
    int theAssignmentId;

    public PrivateNumber(String aNumber)
    {
        theNumber = aNumber;
    }

    public String toString()
    {
        return theNumber;
    }
} class DataBase
{
    static DataBase INSTANCE = new DataBase();
    Map thePrivateNumbers = new HashMap();
    Map thePublicNumbers = new HashMap(); DataBase()
    {}

    String getPublicNumber(String aPrivateNumber)
    {
        return (String) thePublicNumbers.get(aPrivateNumber);
    }

    DefaultListModel getPrivateNumbers(String aPublicNumber)
    {
        DefaultListModel m = (DefaultListModel) thePrivateNumbers.get(aPublicNumber);
        if (m == null)
        {
            m = new DefaultListModel();
            thePrivateNumbers.put(aPublicNumber, m);
        }
        return m;
    }

    public PrivateNumber addPrivateNumber(String aPublicNumber)
    {
        String freeNumber;
        int counter = 100;
        do 
        {
            freeNumber = Integer.toString(counter++);
        }
        while (getPublicNumber(freeNumber) != null);
        PrivateNumber privateNumber = new PrivateNumber(freeNumber);
        getPrivateNumbers(aPublicNumber).add(0, privateNumber);
        thePublicNumbers.put(freeNumber, aPublicNumber);
        return privateNumber;
    }

    void removePrivateNumber(String aPublicNumber, PrivateNumber aPrivateNumber)
    {
        DefaultListModel m = (DefaultListModel) thePrivateNumbers.get(aPublicNumber);
        if (m != null)
        {
            m.removeElement(aPrivateNumber);
        }
        thePublicNumbers.remove(aPrivateNumber);
    }

} class DataPanel2 extends JPanel
{
    JButton theAddButton; DataPanel2(final Main aMain, final String aPublicNumber, final ListModel aModel)
    {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));

        JPanel publik = new JPanel();
        publik.setOpaque(false);
        publik.setLayout(new BorderLayout());
        publik.add(new JLabel("Your public number:"),
            BorderLayout.CENTER);
        JTextField phoneNumberField = new JTextField(aPublicNumber);
        publik.add(phoneNumberField, BorderLayout.SOUTH);
        phoneNumberField.setText(aPublicNumber);
        phoneNumberField.setEditable(false);
        phoneNumberField.setOpaque(false);

        JPanel privates = new JPanel();
        final JList list;
        privates.setOpaque(false);
        privates.setLayout(new BorderLayout());
        privates.add(new JLabel("Your privacy numbers"
            + " (aliases for " + aPublicNumber + "):"),
            BorderLayout.NORTH);
        privates.add(new JScrollPane(list = new JList(aModel)),
            BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));
        final Action delete;
        buttons.add(new JButton(delete = new AbstractAction("Deactivate the selected privacy number")
        {
            public void actionPerformed(ActionEvent e)
            {
                aMain.deletePrivateNumber(aPublicNumber,
                    (PrivateNumber) list.getSelectedValue());
            }
        }));
        delete.setEnabled(false);
        buttons.add(theAddButton = new JButton(new AbstractAction("Activate a new privacy number")
        {
            public void actionPerformed(ActionEvent e)
            {
                aMain.addPrivateNumber(aPublicNumber);
            }
        }));
        theAddButton.setEnabled(false);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                delete.setEnabled(list.getSelectedIndex() >= 0);
            }
        });
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(publik, BorderLayout.NORTH);
        add(privates, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    void setActivated(boolean b)
    {
        theAddButton.setEnabled(b);
    }
}
