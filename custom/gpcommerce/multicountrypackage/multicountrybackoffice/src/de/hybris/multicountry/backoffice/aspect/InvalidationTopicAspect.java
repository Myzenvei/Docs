/**
 *
 */
package de.hybris.multicountry.backoffice.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * The Class InvalidationTopicAspect.
 *
 * @author i304602
 */
@Aspect
public class InvalidationTopicAspect
{
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(InvalidationTopicAspect.class);

	/**
	 * Invalidation aspect.
	 */
	@Pointcut("execution (public * com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer.DefaultRowRenderer.render(..))")
	public void invalidationAspect()
	{
		/* EMPTY */
	}

	/**
	 * Invalidate.
	 *
	 * @param pjp the pjp
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around("invalidationAspect()")
	public Object invalidate(final ProceedingJoinPoint pjp) throws Throwable
	{
		LOG.info("CIAOOOO");
		return pjp.proceed();
	}





}
