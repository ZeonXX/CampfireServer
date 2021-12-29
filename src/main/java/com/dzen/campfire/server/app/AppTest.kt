package com.dzen.campfire.server.app

object AppTest {

    @JvmStatic
    fun main(args: Array<String>) {


        var text = "<string name=\"app_whoops\">Whoops!</string>\n" +
                "    <string name=\"app_retry\">Retry</string>\n" +
                "    <string name=\"app_cancel\">Cancel</string>\n" +
                "    <string name=\"app_continue\">Continue</string>\n" +
                "    <string name=\"app_back\">Back</string>\n" +
                "    <string name=\"app_dont_show_again\">Don\\'t show again</string>\n" +
                "    <string name=\"app_attention\">Attention</string>\n" +
                "    <string name=\"app_download\">Download</string>\n" +
                "    <string name=\"app_downloading\">Downloading</string>\n" +
                "    <string name=\"app_downloaded\">Downloaded</string>\n" +
                "    <string name=\"app_loading\">Loading</string>\n" +
                "    <string name=\"app_loading_dots\">Loading…</string>\n" +
                "    <string name=\"app_done\">Done</string>\n" +
                "    <string name=\"app_choose\">Choose</string>\n" +
                "    <string name=\"app_link\">Link</string>\n" +
                "    <string name=\"app_notifications\">Notifications</string>\n" +
                "    <string name=\"app_notify\">Notify</string>\n" +
                "    <string name=\"app_pin\">Pin</string>\n" +
                "    <string name=\"app_unpin\">Unpin</string>\n" +
                "    <string name=\"app_style\">Style</string>\n" +
                "    <string name=\"app_bookmarks\">Bookmarks</string>\n" +
                "    <string name=\"app_to_finish\">Finish</string>\n" +
                "    <string name=\"app_draft\">Draft</string>\n" +
                "    <string name=\"app_drafts\">Drafts</string>\n" +
                "    <string name=\"app_settings\">Settings</string>\n" +
                "    <string name=\"app_about\">About</string>\n" +
                "    <string name=\"app_report\">Report</string>\n" +
                "    <string name=\"app_reports\">Reports</string>\n" +
                "    <string name=\"app_update\">Update</string>\n" +
                "    <string name=\"app_remove\">Remove</string>\n" +
                "    <string name=\"app_to_drafts\">To drafts</string>\n" +
                "    <string name=\"app_move\">Move</string>\n" +
                "    <string name=\"app_display_above\">Display above…</string>\n" +
                "    <string name=\"app_display_before\">Display before…</string>\n" +
                "    <string name=\"app_block\">Block</string>\n" +
                "    <string name=\"app_blocked\">Blocked</string>\n" +
                "    <string name=\"app_block_title\">Block</string>\n" +
                "    <string name=\"app_change\">Change</string>\n" +
                "    <string name=\"app_send\">Send</string>\n" +
                "    <string name=\"app_search\">Search</string>\n" +
                "    <string name=\"app_comment\">Comment</string>\n" +
                "    <string name=\"app_exit\">Exit</string>\n" +
                "    <string name=\"app_follows\">Follows</string>\n" +
                "    <string name=\"app_followers\">Followers</string>\n" +
                "    <string name=\"app_rates\">Rates</string>\n" +
                "    <string name=\"app_publications\">Publications</string>\n" +
                "    <string name=\"app_publication\">Publication</string>\n" +
                "    <string name=\"app_removed\">Removed</string>\n" +
                "    <string name=\"app_reported\">Your report was received. We will check this shortly.</string>\n" +
                "    <string name=\"app_report_hint\">Comment (optional)</string>\n" +
                "    <string name=\"app_report_already_exist\">You have already reported this</string>\n" +
                "    <string name=\"app_published\">Published</string>\n" +
                "    <string name=\"app_publish\">Publish</string>\n" +
                "    <string name=\"app_changed\">Changed</string>\n" +
                "    <string name=\"app_save\">Save</string>\n" +
                "    <string name=\"app_accept\">Accept</string>\n" +
                "    <string name=\"app_restore\">Restore</string>\n" +
                "    <string name=\"app_i_agree\">I agree</string>\n" +
                "    <string name=\"app_achievements\">Achievements</string>\n" +
                "    <string name=\"app_clear_reports\">Clear reports</string>\n" +
                "    <string name=\"app_clear_reports_confirm\">Clear reports?</string>\n" +
                "    <string name=\"app_clear\">Clear</string>\n" +
                "    <string name=\"app_clear_2\">Clear</string>\n" +
                "    <string name=\"app_no\">No</string>\n" +
                "    <string name=\"app_yes\">Yes</string>\n" +
                "    <string name=\"app_punish\">Punish</string>\n" +
                "    <string name=\"app_punishments\">Punishments</string>\n" +
                "    <string name=\"app_punish_confirm\">Punish this user?</string>\n" +
                "    <string name=\"app_level\">Level</string>\n" +
                "    <string name=\"app_karma_count_30_days\">Karma in 30 days</string>\n" +
                "    <string name=\"app_karma\">Karma</string>\n" +
                "    <string name=\"app_username\">Username</string>\n" +
                "    <string name=\"app_nothing_found\">Nothing Found</string>\n" +
                "    <string name=\"app_privilege\">Privilege</string>\n" +
                "    <string name=\"app_copy\">Copy</string>\n" +
                "    <string name=\"app_quote\">Quote</string>\n" +
                "    <string name=\"app_copied\">Copied</string>\n" +
                "    <string name=\"app_message\">Message</string>\n" +
                "    <string name=\"app_share_message_hint\">Message (optional)</string>\n" +
                "    <string name=\"app_name_s\">Name</string>\n" +
                "    <string name=\"app_ratings\">Ratings</string>\n" +
                "    <string name=\"app_tags\">Tags</string>\n" +
                "    <string name=\"app_user\">User</string>\n" +
                "    <string name=\"app_moderation\">Moderation</string>\n" +
                "    <string name=\"app_do_cancel\">Cancel</string>\n" +
                "    <string name=\"app_fandoms\">Fandoms</string>\n" +
                "    <string name=\"app_activities\">Activities</string>\n" +
                "    <string name=\"app_fandom\">Fandom</string>\n" +
                "    <string name=\"app_users\">Users</string>\n" +
                "    <string name=\"app_chat\">Chat</string>\n" +
                "    <string name=\"app_chats\">Chats</string>\n" +
                "    <string name=\"app_create\">Create</string>\n" +
                "    <string name=\"app_text\">Text</string>\n" +
                "    <string name=\"app_got_it\">Got it</string>\n" +
                "    <string name=\"app_follow\">Follow</string>\n" +
                "    <string name=\"app_unfollow\">Unfollow</string>\n" +
                "    <string name=\"app_online\">Online</string>\n" +
                "    <string name=\"app_stickers\">Stickers</string>\n" +
                "    <string name=\"app_stickers_pack\">Stickers pack</string>\n" +
                "    <string name=\"app_sticker\">Sticker</string>\n" +
                "    <string name=\"app_was_online\">%s online %s</string>\n" +
                "    <string name=\"app_crop\">Crop</string>\n" +
                "    <string name=\"app_tag\">Tag</string>\n" +
                "    <string name=\"app_category\">Category</string>\n" +
                "    <string name=\"app_language\">Language</string>\n" +
                "    <string name=\"app_is_typing\">is typing…</string>\n" +
                "    <string name=\"app_feed\">Feed</string>\n" +
                "    <string name=\"app_genre\">Genre</string>\n" +
                "    <string name=\"app_genres\">Genres</string>\n" +
                "    <string name=\"app_instrument\">Instrument</string>\n" +
                "    <string name=\"app_purpose\">Purpose</string>\n" +
                "    <string name=\"app_composition\">Composition</string>\n" +
                "    <string name=\"app_type\">Type</string>\n" +
                "    <string name=\"app_platforms\">Platforms</string>\n" +
                "    <string name=\"app_controllers\">Controllers</string>\n" +
                "    <string name=\"app_other_names\">Other names</string>\n" +
                "    <string name=\"app_uploading\">Uploading</string>\n" +
                "    <string name=\"app_administration\">Administration</string>\n" +
                "    <string name=\"app_reject\">Reject</string>\n" +
                "    <string name=\"app_profile\">Profile</string>\n" +
                "    <string name=\"app_image\">Image</string>\n" +
                "    <string name=\"app_voice_message\">Voice message</string>\n" +
                "    <string name=\"app_empty\">Empty</string>\n" +
                "    <string name=\"app_open\">Open</string>\n" +
                "    <string name=\"app_share\">Share</string>\n" +
                "    <string name=\"app_copy_link\">Copy link</string>\n" +
                "    <string name=\"app_copy_link_with_language\">Copy link (+language)</string>\n" +
                "    <string name=\"app_choose_template\">Choose a template</string>\n" +
                "    <string name=\"app_choose_user\">Choose user</string>\n" +
                "    <string name=\"app_moderator\">Moderator</string>\n" +
                "    <string name=\"app_admin\">Admin</string>\n" +
                "    <string name=\"app_protoadmin\">Protoadmin</string>\n" +
                "    <string name=\"app_d\">d.</string>\n" +
                "    <string name=\"app_description\">Description</string>\n" +
                "    <string name=\"app_add\">Add</string>\n" +
                "    <string name=\"app_status\">Status</string>\n" +
                "    <string name=\"app_subscription\">Subscription</string>\n" +
                "    <string name=\"app_subscribe\">Subscribe</string>\n" +
                "    <string name=\"app_unsubscribe\">Unsubscribe</string>\n" +
                "    <string name=\"app_subscriptions\">Subscriptions</string>\n" +
                "    <string name=\"app_subscribers\">Subscribers</string>\n" +
                "    <string name=\"app_wiki\">Wiki</string>\n" +
                "    <string name=\"app_rubric\">Rubric</string>\n" +
                "    <string name=\"app_rubrics\">Rubrics</string>\n" +
                "    <string name=\"app_do_mark\">Mark</string>\n" +
                "    <string name=\"app_do_unmark\">Unmark</string>\n" +
                "    <string name=\"app_remove_image\">Remove image</string>\n" +
                "    <string name=\"app_remove_link\">Remove link</string>\n" +
                "    <string name=\"app_naming\">Title</string>\n" +
                "    <string name=\"app_rename\">Rename</string>\n" +
                "    <string name=\"app_not_required\">Not a requirement</string>\n" +
                "    <string name=\"app_to_return\">Return</string>\n" +
                "    <string name=\"app_show_all\">Show</string>\n" +
                "    <string name=\"app_all\">All</string>\n" +
                "    <string name=\"app_all_person\">All</string>\n" +
                "    <string name=\"app_hide\">Hide</string>\n" +
                "    <string name=\"app_forgive\">Forgive</string>\n" +
                "    <string name=\"app_forgive_confirm\">Forgive the prisoner?</string>\n" +
                "    <string name=\"app_deprive_moderator\">Deprive moderating</string>\n" +
                "    <string name=\"app_deprive\">Deprive</string>\n" +
                "    <string name=\"app_make\">Make</string>\n" +
                "    <string name=\"app_review\">Review</string>\n" +
                "    <string name=\"app_reviews\">Reviews</string>\n" +
                "    <string name=\"app_other\">Other</string>\n" +
                "    <string name=\"app_translates\">Translates</string>\n" +
                "    <string name=\"app_create_post\">Create post</string>\n" +
                "    <string name=\"app_add_into_draft\">Add to draft</string>\n" +
                "    <string name=\"app_add_to_message\">Add to message</string>\n" +
                "    <string name=\"app_fast_post_to\">Fast post to %s</string>\n" +
                "    <string name=\"app_fast_post\">Fast post</string>\n" +
                "    <string name=\"app_close\">Close</string>\n" +
                "    <string name=\"app_remove_text\">Remove text</string>\n" +
                "    <string name=\"app_bot\">Bot</string>\n" +
                "    <string name=\"app_note\">Note</string>\n" +
                "    <string name=\"app_congratulations\">Congratulations!</string>\n" +
                "    <string name=\"app_edited\">edited</string>\n" +
                "    <string name=\"app_edit\">Edit</string>\n" +
                "    <string name=\"app_comments\">Comments</string>\n" +
                "    <string name=\"app_choose_image\">Choose image</string>\n" +
                "    <string name=\"app_title\">Title</string>\n" +
                "    <string name=\"app_confirm\">Confirm</string>\n" +
                "    <string name=\"app_file\">File</string>\n" +
                "    <string name=\"app_favorite\">Favorite</string>\n" +
                "    <string name=\"app_favorites\">Favorites</string>\n" +
                "    <string name=\"app_error\">Error</string>\n" +
                "    <string name=\"app_post\">Post</string>\n" +
                "    <string name=\"app_posts\">Posts</string>\n" +
                "    <string name=\"app_not_now\">Not now</string>\n" +
                "    <string name=\"app_pending\">Pending</string>\n" +
                "    <string name=\"app_ad\">Ad</string>\n" +
                "    <string name=\"app_campfire\">Campfire</string>\n" +
                "    <string name=\"app_vkontakte\">VKontakte</string>\n" +
                "    <string name=\"app_email\">EMail</string>\n" +
                "    <string name=\"app_article\">Article</string>\n" +
                "    <string name=\"app_section\">Section</string>\n" +
                "    <string name=\"app_coefficient\">Coefficient</string>\n" +
                "    <string name=\"app_coefficient_karma\">Karma coefficient %s</string>\n" +
                "    <string name=\"app_history\">History</string>\n" +
                "    <string name=\"app_feed_abyss\">Abyss</string>\n" +
                "    <string name=\"app_feed_good\">Good</string>\n" +
                "    <string name=\"app_feed_best\">Best</string>\n" +
                "    <string name=\"app_collection\">Collection</string>\n" +
                "    <string name=\"app_collection_add\">Add in collection</string>\n" +
                "    <string name=\"app_collection_remove\">Remove from collection</string>\n" +
                "    <string name=\"app_reaction\">Reaction</string>\n" +
                "    <string name=\"app_change_naming\">Change name</string>\n" +
                "    <string name=\"app_anonymous\">Anonymous</string>\n" +
                "    <string name=\"app_anonymously\">Anonymously</string>\n" +
                "    <string name=\"app_limitations\">Limitations</string>\n" +
                "    <string name=\"app_order\">Order</string>\n" +
                "    <string name=\"app_soon\">Soon...</string>\n" +
                "    <string name=\"app_relay_race\">Relay race</string>\n" +
                "    <string name=\"app_relay_races\">Relay races</string>\n" +
                "    <string name=\"app_choose_fandom\">Select fandom</string>\n" +
                "    <string name=\"app_participate\">Participate</string>\n" +
                "    <string name=\"app_participate_no\">Not participate</string>\n" +
                "    <string name=\"app_additional\">Additional</string>\n" +
                "    <string name=\"app_details\">Details</string>\n" +
                "    <string name=\"app_ok\">Ok</string>\n" +
                "    <string name=\"app_gif\">Gif</string>\n" +
                "    <string name=\"app_images\">Images</string>\n" +
                "    <string name=\"app_you\">You</string>\n" +
                "    <string name=\"app_viceroy\">Viceroy</string>\n" +
                "    <string name=\"app_assign\">Assign</string>\n" +
                "    <string name=\"app_donate\">Donate</string>\n" +
                "    <string name=\"app_sponsor\">Sponsor</string>\n" +
                "    <string name=\"app_root\">Root</string>\n" +
                "    <string name=\"app_was\">Was</string>\n" +
                "    <string name=\"app_wits_us\">with us</string>\n" +
                "    <string name=\"app_multilingual\">Multilingual</string>\n" +
                "    <string name=\"app_actual\">Actual</string>\n" +
                "    <string name=\"app_archive\">Archive</string>\n" +
                "    <string name=\"app_gallery\">Gallery</string>\n" +
                "    <string name=\"app_allowed\">Allowed</string>\n" +
                "    <string name=\"app_forbidden\">Forbidden</string>\n" +
                "    <string name=\"app_nobody\">Nobody</string>\n" +
                "    <string name=\"app_info\">Information</string>\n" +
                "    <string name=\"app_checking\">Checking</string>\n" +
                "    <string name=\"app_not_available_yet\">Not available yet</string>\n" +
                "    <string name=\"he\">He</string>\n" +
                "    <string name=\"she\">She</string>\n" +
                "    <string name=\"he_baned\">banned</string>\n" +
                "    <string name=\"she_baned\">banned</string>\n" +
                "    <string name=\"he_blocked\">blocked</string>\n" +
                "    <string name=\"she_blocked\">blocked</string>\n" +
                "    <string name=\"he_gained\">gained</string>\n" +
                "    <string name=\"she_gained\">gained</string>\n" +
                "    <string name=\"he_suggest\">suggest</string>\n" +
                "    <string name=\"she_suggest\">suggest</string>\n" +
                "    <string name=\"he_accept\">accept</string>\n" +
                "    <string name=\"she_accept\">accept</string>\n" +
                "    <string name=\"he_changed\">changed</string>\n" +
                "    <string name=\"she_changed\">changed</string>\n" +
                "    <string name=\"he_remove\">remove</string>\n" +
                "    <string name=\"she_remove\">remove</string>\n" +
                "    <string name=\"he_rename\">rename</string>\n" +
                "    <string name=\"she_rename\">rename</string>\n" +
                "    <string name=\"he_created\">created</string>\n" +
                "    <string name=\"she_created\">created</string>\n" +
                "    <string name=\"he_add\">add</string>\n" +
                "    <string name=\"she_add\">add</string>\n" +
                "    <string name=\"he_mark\">marked</string>\n" +
                "    <string name=\"she_mark\">marked</string>\n" +
                "    <string name=\"he_return\">return</string>\n" +
                "    <string name=\"she_return\">return</string>\n" +
                "    <string name=\"he_comment\">comment</string>\n" +
                "    <string name=\"she_comment\">comment</string>\n" +
                "    <string name=\"he_rate\">rate</string>\n" +
                "    <string name=\"she_rate\">rate</string>\n" +
                "    <string name=\"he_subscribed\">subscribed</string>\n" +
                "    <string name=\"she_subscribed\">subscribed</string>\n" +
                "    <string name=\"he_replied\">replied</string>\n" +
                "    <string name=\"she_replied\">replied</string>\n" +
                "    <string name=\"he_make\">make</string>\n" +
                "    <string name=\"she_make\">make</string>\n" +
                "    <string name=\"he_was\">was</string>\n" +
                "    <string name=\"she_was\">was</string>\n" +
                "    <string name=\"he_forgive\">forgive</string>\n" +
                "    <string name=\"she_forgive\">forgive</string>\n" +
                "    <string name=\"he_warn\">warn</string>\n" +
                "    <string name=\"she_warn\">warn</string>\n" +
                "    <string name=\"he_warned\">warned</string>\n" +
                "    <string name=\"she_warned\">warned</string>\n" +
                "    <string name=\"he_deprived\">deprived</string>\n" +
                "    <string name=\"she_deprived\">deprived</string>\n" +
                "    <string name=\"he_reject\">rejected</string>\n" +
                "    <string name=\"she_reject\">rejected</string>\n" +
                "    <string name=\"he_restore\">restored</string>\n" +
                "    <string name=\"she_restore\">restored</string>\n" +
                "    <string name=\"he_move\">moved</string>\n" +
                "    <string name=\"she_move\">moved</string>\n" +
                "    <string name=\"he_mentioned\">mentioned</string>\n" +
                "    <string name=\"she_mentioned\">mentioned</string>\n" +
                "    <string name=\"he_open\">open</string>\n" +
                "    <string name=\"she_open\">open</string>\n" +
                "    <string name=\"he_close\">close</string>\n" +
                "    <string name=\"she_close\">close</string>\n" +
                "    <string name=\"he_pined\">pined</string>\n" +
                "    <string name=\"she_pined\">pined</string>\n" +
                "    <string name=\"he_unpinned\">unpinned</string>\n" +
                "    <string name=\"she_unpinned\">unpinned</string>\n" +
                "    <string name=\"he_finished\">finished</string>\n" +
                "    <string name=\"she_finished\">finished</string>\n" +
                "    <string name=\"he_leave\">leave</string>\n" +
                "    <string name=\"she_leave\">leave</string>\n" +
                "    <string name=\"he_reenter\">Enter</string>\n" +
                "    <string name=\"she_reenter\">Enter</string>\n" +
                "    <string name=\"he_assign\">assign</string>\n" +
                "    <string name=\"she_assign\">assign</string>\n" +
                "    <string name=\"he_react\">react</string>\n" +
                "    <string name=\"she_react\">react</string>\n" +
                "    <string name=\"he_give\">give</string>\n" +
                "    <string name=\"she_give\">give</string>\n" +
                "    <string name=\"he_denied\">denied</string>\n" +
                "    <string name=\"she_denied\">denied</string>"

        text = text.replace("\\'", "'")

        //ru(text)
        en(text)


    }

    fun ru(text:String){
        val split_1 = text.split("<string name=\"")

        for(i_1 in split_1){
            val split_2 = i_1.split("\">")
            if(split_2.size == 1) continue
            val key = split_2[0]
            val v = split_2[1].split("</string>")[0]
            System.err.println("val $key = Translate(\"$v\")")
        }

    }

    fun en(text:String){
        val split_1 = text.split("<string name=\"")

        for(i_1 in split_1){
            val split_2 = i_1.split("\">")
            if(split_2.size == 1) continue
            val key = split_2[0]
            val v = split_2[1].split("</string>")[0]
            System.err.println("en(\"$key\", \"$v\")")
        }

    }

}