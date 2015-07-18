package com.iidooo.cms.action.channel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.iidooo.cms.dto.extend.ChannelDto;
import com.iidooo.cms.service.channel.IChannelDetailService;
import com.iidooo.core.action.BaseAction;
import com.iidooo.core.util.ValidateUtil;

public class ChannelDetailAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ChannelDetailAction.class);

    @Autowired
    private IChannelDetailService channelInfoService;

    private ChannelDto channel;

    public ChannelDto getChannel() {
        return channel;
    }

    public void setChannel(ChannelDto channel) {
        this.channel = channel;
    }

    public String init() {
        try {
            // The modify mode will trace the channel ID.
            if (channel != null && channel.getChannelID() != null && channel.getChannelID() > 0) {
                channel = channelInfoService.getChannelByID(channel.getChannelID());
            }

            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return ERROR;
        }
    }

    public String create() {
        try {

            // The validate of channel path should not be duplicated.
            if (channelInfoService.isChannelPathDuplicate(channel.getSiteID(), channel.getChannelPath())) {
                addActionError(getText("MSG_CHANNEL_CREATE_FAILED_DUPLICATE", new String[] { channel.getChannelPath() }));
                return INPUT;
            }

            if (!channelInfoService.createChannel(channel)) {
                addActionError(getText("MSG_CHANNEL_CREATE_FAILED"));
                return INPUT;
            }
            addActionMessage(getText("MSG_CHANNEL_CREATE_SUCCESS", new String[] { channel.getChannelName() }));
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return ERROR;
        }
    }

    public void validateCreate() {
        try {
            // The validate of channel name should not be empty.
            if (ValidateUtil.isEmpty(channel.getChannelName())) {
                addActionError(getText("MSG_CHANNEL_NAME_REQUIRE"));
            }

            // The validate of channel path should not be empty.
            if (ValidateUtil.isEmpty(channel.getChannelPath())) {
                addActionError(getText("MSG_CHANNEL_PATH_REQUIRE"));
            }

            // The channel path must be English
            if (!ValidateUtil.isEnglish(channel.getChannelPath())) {
                addActionError(getText("MSG_CHANNEL_PATH_ENGLISH"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            addActionError(getText("MSG_VALIDATION_EXCEPTION"));
        }
    }

    public String update() {
        try {
            if (!channelInfoService.updateChannel(channel)) {
                addActionError(getText("MSG_CHANNEL_UPDATE_FAILED", new String[] { channel.getChannelName() }));
                return INPUT;
            }
            addActionMessage(getText("MSG_CHANNEL_UPDATE_SUCCESS", new String[] { channel.getChannelName() }));
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return ERROR;
        }
    }

    public void validateUpdate() {
        try {
            if (channel == null || ValidateUtil.isEmpty(channel.getChannelID())) {
                addActionError(getText("MSG_CHANNEL_ID_REQUIRE"));
            }

            if (ValidateUtil.isEmpty(channel.getChannelName())) {
                addActionError(getText("MSG_CHANNEL_NAME_REQUIRE"));
            }

            if (ValidateUtil.isEmpty(channel.getChannelPath())) {
                addActionError(getText("MSG_CHANNEL_PATH_REQUIRE"));
            }
            
            // The channel path must be English
            if (!ValidateUtil.isEnglish(channel.getChannelPath())) {
                addActionError(getText("MSG_CHANNEL_PATH_ENGLISH"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            addActionError(getText("MSG_VALIDATION_EXCEPTION"));
        }
    }
}
