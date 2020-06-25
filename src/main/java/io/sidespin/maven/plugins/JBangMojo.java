package io.sidespin.maven.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which executes <a href="https://jbang.dev/">J'Bang</a> on a Java/JShell file or URL.
 */
@Mojo(name = "jbang")
public class JBangMojo extends AbstractMojo {
	
	private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
	
	/**
	 * Location of the J'Bang source directory
	 */
	@Parameter(defaultValue = "src/jbang/java", property = "jbang.source.directory", required = true)
	private File jbangSources;
	
	@Parameter(defaultValue = "script.java", property = "jbang.source.script", required = true)
	private String jbangScript;
	

	public void execute() throws MojoExecutionException {
		List<String> cmd = getCommand();
		
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.directory(jbangSources);
		builder.inheritIO();
		int exitCode = 1;
		try {
			Process process = builder.start();
			// jbang doesn't exit with code != 0 when it fails to run???
			exitCode = process.waitFor();
			
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		if (exitCode != 0) {
		      throw new MojoExecutionException("J'Bang exited with status " + exitCode);
		}
	}

	private List<String> getCommand() {
		List<String> cmd;
		String scriptPath = getScriptPath();
		if (isWindows) {
			cmd = Arrays.asList("cmd.exe", "/c", "jbang.bat "+scriptPath);
		} else {
			cmd = Arrays.asList("sh", "-c", "jbang "+scriptPath);
		}
		return cmd;
	}

	private String getScriptPath() {
		String script;
		if (jbangScript.startsWith("http")) {
			script = jbangScript;
		} else {
			File file = new File(jbangScript);
			if (file.isAbsolute()) {
				script = file.getAbsolutePath();
			} else {
				script = new File(jbangSources, jbangScript).getAbsolutePath();
			}			
		}
		return script;
	}
}
