<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- <xsl:output method="xml" indent="yes" />
	<xsl:preserve-space elements="*"/> -->
	<xsl:param name="cockpit.script.path"/>
	<xsl:param name="cockpit.css.path"/>
	<xsl:param name="version"/>	
	
	<xsl:variable name="changed.comment" select="' Changed by otmmaddonui '"/>
	<xsl:variable name="comment.change.start" select="' otmmaddonui START '"/>
	<xsl:variable name="comment.change.end" select="' otmmaddonui END '"/>
	<xsl:variable name="cockpit.css.exists" select="boolean(*/style[contains(@src, $cockpit.css.path)])"/>
	<xsl:variable name="cockpit.script.exists" select="boolean(processing-instruction()[contains(., $cockpit.script.path)])"/>
	<xsl:variable name="changed.comment.exists" select="boolean(comment()[contains(.,$changed.comment)])"/>
	
	<xsl:template name="newline">
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	

	
	<xsl:template match="/">
		<xsl:message >cockpit.css.exists = <xsl:copy-of select="$cockpit.css.exists"/></xsl:message>
		<xsl:message>cockpit.script.exists = <xsl:copy-of select="$cockpit.script.exists"/></xsl:message>
		<xsl:message>changed.comment.exists = <xsl:copy-of select="$changed.comment.exists"/></xsl:message>

		<xsl:choose>
			<xsl:when test="not($changed.comment.exists)">
	    		<xsl:call-template name="newline"/>
	    		<xsl:comment><xsl:value-of select="$changed.comment"/></xsl:comment>
	    		<xsl:call-template name="newline"/>
    		</xsl:when>
    	</xsl:choose>
    	
    	<xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="/comment()">
    	<xsl:choose>
	    	<xsl:when test="contains(., $comment.change.start)">
	    		<xsl:call-template name="newline"/>
	    		<xsl:comment><xsl:value-of select="."/></xsl:comment>
	    	</xsl:when>
	    	<xsl:when test="contains(., $comment.change.end)">
	    		<xsl:comment><xsl:value-of select="."/></xsl:comment>
	    		<xsl:call-template name="newline"/>
	    	</xsl:when>
	    	<xsl:otherwise>
	    		<xsl:call-template name="newline"/>
	    		<xsl:comment><xsl:value-of select="."/></xsl:comment>
	    		<xsl:call-template name="newline"/>
	    	</xsl:otherwise>
    	</xsl:choose>

    </xsl:template>
	
	<xsl:template match="/processing-instruction()">
		<xsl:call-template name="newline"/>
		<xsl:choose>
	    	<xsl:when test="contains(., $cockpit.script.path)">
	    		<xsl:processing-instruction name="script">src=&quot;<xsl:value-of select="concat($cockpit.script.path, '?v=', $version)" />&quot; </xsl:processing-instruction>
	    	</xsl:when>
	    	<xsl:otherwise>
	    		<xsl:copy>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</xsl:copy>
	    	</xsl:otherwise>
    	</xsl:choose>

		<xsl:call-template name="newline"/>
		<!-- <xsl:message >
			<xsl:value-of select="name(.)"/>
		</xsl:message> -->
	</xsl:template>
	
	<xsl:template match="/div[1]">
		<xsl:choose>
			<xsl:when test="not($cockpit.script.exists)">
				<xsl:call-template name="newline"/>
				<xsl:comment><xsl:value-of select="$comment.change.start"/></xsl:comment>
				<xsl:call-template name="newline"/>
				<xsl:processing-instruction name="script">src=&quot;<xsl:value-of select="concat($cockpit.script.path, '?v=', $version)" />&quot; </xsl:processing-instruction>
				<xsl:call-template name="newline"/>
				<xsl:comment><xsl:value-of select="$comment.change.end"/></xsl:comment>
				<xsl:call-template name="newline"/>
				<xsl:call-template name="newline"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="newline"/>
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/div[1]/*[1]">

		<xsl:if test="not($cockpit.css.exists)">
			<xsl:call-template name="newline"/>
			<xsl:text>    </xsl:text><xsl:comment><xsl:value-of select="$comment.change.start"/></xsl:comment>
			<xsl:call-template name="newline"/>
			<xsl:text>    </xsl:text><xsl:element name="style"><xsl:attribute name="src"><xsl:value-of select="concat($cockpit.css.path, '?v=', $version)" /></xsl:attribute></xsl:element> 
			<xsl:call-template name="newline"/>
			<xsl:text>    </xsl:text><xsl:comment><xsl:value-of select="$comment.change.end"/></xsl:comment>
			<xsl:call-template name="newline"/>
			<xsl:call-template name="newline"/>
			<xsl:text>    </xsl:text>
		</xsl:if>
		
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="*/style[contains(@src,$cockpit.css.path)]">
		<xsl:element name="style"><xsl:attribute name="src"><xsl:value-of select="concat($cockpit.css.path, '?v=', $version)" /></xsl:attribute></xsl:element>
	</xsl:template>
	
	<!-- standard copy template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>	
</xsl:stylesheet>