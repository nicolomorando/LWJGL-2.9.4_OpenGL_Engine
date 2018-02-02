/**
 * @author Morando Nicolò
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL30;

public class FboBuilder {

	private final int width;
	private final int height;
	private final int samples;

	private Map<Integer, Attachment> colourAttachments = new HashMap<Integer, Attachment>();
	private Attachment depthAttachment;

	protected FboBuilder(int width, int height, int samples) {
		this.width = width;
		this.height = height;
		this.samples = samples;
	}

	public FboBuilder addColourAttachment(int index, Attachment attachment) {
		colourAttachments.put(index, attachment);
		return this;
	}

	public FboBuilder addDepthAttachment(Attachment attachment) {
		this.depthAttachment = attachment;
		attachment.setAsDepthAttachment();
		return this;
	}

	public Fbo init() {
		int fboId = createFbo();
		createColourAttachments();
		createDepthAttachment();
		return new Fbo(fboId, width, height, colourAttachments, depthAttachment);
	}

	/**
	 * Creates an new FBO object.
	 * 
	 * @return The newly created FBO.
	 */
        
	private int createFbo() {
		int fboId = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
		return fboId;
	}

	/**
	 * Initialises and attaches all the colour attachments for this FBO.
	 */
        
	private void createColourAttachments() {
		for (Entry<Integer, Attachment> entry : colourAttachments.entrySet()) {
			Attachment attachment = entry.getValue();
			int attachmentId = GL30.GL_COLOR_ATTACHMENT0 + entry.getKey();
			attachment.init(attachmentId, width, height, samples);
		}
	}

	/**
	 * Creates and attaches the depth attachment, if there is one.
	 */
        
	private void createDepthAttachment() {
		if (depthAttachment != null) {
			depthAttachment.init(GL30.GL_DEPTH_ATTACHMENT, width, height, samples);
		}
	}

}
