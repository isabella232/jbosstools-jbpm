package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.jbpm.ui.bot.test.suite.Project;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotMultiPageEditor;
import org.junit.Test;

@SWTBotTestRequires( clearProjects=false, jbpm=@JBPM(), server=@Server(type=ServerType.SOA,state=ServerState.Present))
public class JBPMDeployTest extends JBPMTest {

	@Test
	public void deploy() {
		PackageExplorer pe = new PackageExplorer();
		pe.openFile(Project.PROJECT_NAME, "src/main/jpdl","simple.jpdl.xml");
		util.waitForNonIgnoredJobs();
		
		SWTGefBot gefBot = new SWTGefBot();
		SWTBotGefEditor editor =  gefBot.gefEditor("simple");
		editor.setFocus();
		SWTBotMultiPageEditor multi = new SWTBotMultiPageEditor(editor.getReference(), gefBot);
		multi.activatePage("Deployment");
		
		gefBot.textWithLabel("Server Name:").setText("127.0.0.1");
		SWTBotText item = gefBot.textWithLabel("Server Deployer:").setText("gpd-deployer");
		editor.save();
		
		String serverName = TestConfigurator.currentConfig.getServer().type+"-"+TestConfigurator.currentConfig.getServer().version;
		servers.startServer(serverName);
		
		item.setFocus();		
		bot.menu("jBPM").menu("Ping Server").click();
		
		bot.sleep(TIME_1S);
		bot.text(0).setText("admin");
		bot.text(1).setText("admin");
		bot.clickButton(IDELabel.Button.OK);
		bot.sleep(TIME_1S);
		bot.clickButton(IDELabel.Button.OK);		
		
		bot.sleep(TIME_10S);
	}
}
