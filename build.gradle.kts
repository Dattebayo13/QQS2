import java.time.*
import myaa.subkt.ass.*
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*

plugins { id("myaa.subkt") }

subs {
    readProperties("sub.properties")
    episodes(getList("episodes"))

    
	merge {
	    onStyleConflict(ErrorMode.FAIL)

        from(get("dialogue")) {
		    incrementLayer(20)
		}

        if (propertyExists("OP")) {
            from(get("OP")) { syncTargetTime(getAs<Duration>("opsync")) }
        }

        if (propertyExists("ED")) {
            from(get("ED")) { syncTargetTime(getAs<Duration>("edsync")) }
        }

        if (file(get("typesetting")).exists()) {
            from(get("typesetting"))
        }
		out(get("mergedname"))
    }
	
	swap {
        from(merge.item())
		styles(Regex("Default|Italics"))
        out(get("mergednameswap"))
    }
	
    val merge_ss by task<Merge> {
        if (propertyExists("OP")) {
            from(get("OP")) { syncTargetTime(getAs<Duration>("opsync")) }
        }

        if (propertyExists("ED")) {
            from(get("ED")) { syncTargetTime(getAs<Duration>("edsync")) }
        }

        if (file(get("typesetting")).exists()) {
            from(get("typesetting"))
        }
		out(get("mergednamess"))
        }
		
	chapters {
        from(get("dialogue"))
        chapterMarker("chptr")
    }


    mux {
        title(get("title"))

        from(get("premux")) {
            video {
			    name("HEVC BDRip by Vardë@Raws-Maji")
                lang("jpn")
                default(true)
            }
            audio {
                name("")
				lang("jpn")
                default(true)
        }
			includeChapters(false)
            attachments { include(false) }
        }

//		from(get("jpnaudio")) {
//            audio {
//                name("")
//				lang("jpn")
//                default(true)
//        }}
        		
		from(get("dub")) {
            audio {
                name("")
				lang("eng")
                default(false)
        }}
        
		from(merge.item()) {
            tracks {
                lang("eng")
                name(get("subtitle_full"))
                default(true)
                forced(false)
				
            }
        }

		from(swap.item()) {
            tracks {
                lang("enm")
                name(get("subtitle_enm"))
                default(false)
                forced(false)

            }
        }
		
         from(merge_ss.item()) {
            tracks {
                lang("eng")
                name(get("subtitle_ss"))
                default(false)
                forced(true)
				
            }
        }
		chapters(chapters.item()) { lang("eng") }

		attach(get("common_fonts")) { includeExtensions("ttf", "otf") }

        attach(get("ep_fonts")) { includeExtensions("ttf", "otf") }

        if (propertyExists("OP")) {
            attach(get("opfonts")) { includeExtensions("ttf", "otf") }
        }

        if (propertyExists("ED")) {
            attach(get("edfonts")) { includeExtensions("ttf", "otf") }
        }
		
		onMissingGlyphs(ErrorMode.IGNORE)
		skipUnusedFonts(true)
        out(get("muxout"))
		
    }
 tasks(getList("ncs")) {
 
 mux {
            title(get("title"))

            from(get("ncpremux")) {
                video {
				    name("HEVC BDRip by Vardë@Raws-Maji")
                    lang("jpn")
                    default(true)
                }
				audio {
                name("")
				lang("jpn")
                default(true)
        }
				subtitles { include(false) }
                includeChapters(false)
                attachments { include(false) }
            }
//		    from(get("nc_audio")) {
//                audio {
//                name("")
//				lang("jpn")
//                default(true)
//        }}
			from(get("nc_subs")) {
                tracks {
                    lang("eng")
                    name(get("subtitle_full"))
                    default(true)
                    forced(false)
               
                }
            }
			attach(get("ncfonts")) { includeExtensions("ttf", "otf") }
			out(get("muxout"))

        }


}
}
