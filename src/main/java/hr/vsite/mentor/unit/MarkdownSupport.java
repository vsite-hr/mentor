package hr.vsite.mentor.unit;

import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.aside.AsideExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

/**
 *Class for parsing markdwnn
 */
public class MarkdownSupport {
	
	public MarkdownSupport() {
		
	options = new MutableDataSet().set(TablesExtension.CLASS_NAME, "GFMtables")
										.set(EmojiExtension.USE_IMAGE_URLS, true)
										.set(EmojiExtension.ATTR_IMAGE_SIZE, "25")
										;
	
	extensions = Arrays.asList(TablesExtension.create(), 
								AbbreviationExtension.create(),
								AsideExtension.create(),
								EmojiExtension.create(),
								FootnoteExtension.create(),
								StrikethroughExtension.create(),
								TaskListExtension.create(),
								AutolinkExtension.create()
//								SimTocExtension.create()	
								);							
	}
	
	/**
	 * Generate HTML from markdown
	 * @param markdown (String)
	 * @return String (HTML)
	 */
	public String markdownToHtml(String markdown){
		
		Parser parser = Parser.builder(options).extensions(extensions).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).extensions(extensions).build();
		
		Node document = parser.parse(markdown);
		
		return renderer.render(document);
	}
	
	private MutableDataHolder options;
	private List<Extension> extensions;
}
