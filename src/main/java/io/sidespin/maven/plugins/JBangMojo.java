package io.sidespin.maven.plugins;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which executes j'bang on a Java file.
 */
@Mojo(name = "jbang")
public class JBangMojo extends AbstractMojo {
	/**
	 * Location of the j'bang source directory
	 */
	@Parameter(defaultValue = "src/jbang/java", property = "jbang.source.directory", required = true)
	private File jbangSources;
	
	@Parameter(defaultValue = "script.java", property = "jbang.source.script", required = true)
	private String jbangScript;
	
	private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

	public void execute() throws MojoExecutionException {
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
		ProcessBuilder builder = new ProcessBuilder();
		if (isWindows) {
		    builder.command("cmd.exe", "/c", "jbang.bat "+script);
		} else {
		    builder.command("sh", "-c",  "jbang "+script);
		}
		builder.directory(jbangSources);
		builder.inheritIO();
		Process process;
		int exitCode = 1;
		try {
			process = builder.start();
			exitCode = process.waitFor();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		if (exitCode != 0) {
		      throw new MojoExecutionException("j'bang exited with status " + exitCode);
		}
	}
}
