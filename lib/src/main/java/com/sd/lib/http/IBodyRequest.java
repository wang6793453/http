package com.sd.lib.http;

import com.sd.lib.http.body.IRequestBody;

public interface IBodyRequest
{
    IBodyRequest body(IRequestBody body);
}
