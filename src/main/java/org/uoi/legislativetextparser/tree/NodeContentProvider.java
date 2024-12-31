package org.uoi.legislativetextparser.tree;

import org.json.JSONObject;

public interface NodeContentProvider {

    String getContentForNode(String nodeText, JSONObject jsonObject);
}
